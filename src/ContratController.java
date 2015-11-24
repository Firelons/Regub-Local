
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vinc
 */
public class ContratController {
    /*private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/regub";
    private static final String USER = "root";
    private static final String PASS = "";*/
    
    
    private static String JDBC_DRIVER;
    private static String DB_URL;
    private static String USER;
    private static String PASS;
    private static String RAYON;
    private static String REGION;
    private static String MAGASIN;
    
    
    private static ContratController INSTANCE;
    
    ContratController() {
        try{
            Configuration conf = Configuration.getInstance();
            JDBC_DRIVER = conf.getProp("jdbc_driver");
            DB_URL = conf.getProp("db_url");
            USER = conf.getProp("user");
            PASS = conf.getProp("pass");
            RAYON = conf.getProp("rayon");
            REGION = conf.getProp("region");
            MAGASIN = conf.getProp("magasin");

        }
        catch(RegubException ex)
        {
            Logger.getLogger(FichierController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static ContratController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ContratController();
        }
        return INSTANCE;
    }
    
    public ArrayList<Contrat> telechargerContrats() throws RegubException {
        
        PreparedStatement preparedStatement = null;
        ArrayList<Contrat> contrat_remote = new ArrayList();
        Connection conn = null;
        Date date= new Date(), dateDeb = new Date(), dateFin = new Date();
        convertToCalendar(date);
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            //String sql = "SELECT * FROM Video";
            String sql = "SELECT Video.idVideo, Video.titre, Video.frequence, Video.duree, Video.dateDebut, Video.dateFin, TypeRayon.idTypeRayon, Magasin.idMagasin " +
                          "FROM Video " +
                          "INNER JOIN DiffusionsTypesRayons ON DiffusionsTypesRayons.idVideo = Video.idVideo " +
                          "INNER JOIN TypeRayon ON DiffusionsTypesRayons.idTypeRayon = TypeRayon.idTypeRayon " +
                          "INNER JOIN Rayons ON TypeRayon.idTypeRayon = Rayons.idTypeRayon " +
                          "INNER JOIN Magasin ON Rayons.idMagasin = Magasin.idMagasin " +
                          "INNER JOIN Region ON Magasin.idRegion = Region.idRegion " +
                          "WHERE TypeRayon.libelle = ? "+ 
                          "AND Region.libelle = ? " +
                          "AND Magasin.nom = ? ";
                  
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,RAYON);
            preparedStatement.setString(2,REGION);
            preparedStatement.setString(3,MAGASIN);
            ResultSet rs = preparedStatement.executeQuery(); 
            
            /*TELECHARGEMENT DES CONTRATS*/
            while (rs.next()) {                
                dateDeb = rs.getDate("dateDebut");
                dateFin = rs.getDate("dateFin"); 
                /*Si la dateDebut est passée et la dateFin est à venir, on télécharge les contrats*/
                if(convertToCalendar(dateDeb).before(convertToCalendar(date)) && convertToCalendar(dateFin).after(convertToCalendar(date)))
                {
                    Contrat cm = new Contrat(rs.getInt("idVideo"), rs.getString("titre"), 
                                             rs.getInt("frequence"),rs.getInt("duree"), 
                                             convertToCalendar(dateDeb), convertToCalendar(dateFin));                
                    contrat_remote.add(cm);
                }
            }
           rs.close(); 
           
           /*TELECHARGEMENT DES VIDEOS*/
           File path = new File("videos/"); 
           boolean dif = true;
           if( path.exists() ) {
               File[] files = path.listFiles();
               /*Si la video n'a pas de contrat(contrat terminé), elle est suprimée*/
               for(File fil: files){
                   for(Contrat ct : contrat_remote){
                       
                       if(fil.getName() != ct.getIdVideo()+".mp4") break;
                       else dif = false; 
                   }
                   if(dif && !fil.getName().equals("pause.mp4")) fil.delete();
               }                              
               /*Si la video existe, elle n'est plus télechargée*/                   
                   boolean exist = false;
                   for(Contrat ct : contrat_remote){
                       for(File fil: files){
                            
                            if(ct.getIdVideo()+".mp4" != fil.getName()) break;
                            else exist = true; 
                   }
                   if(!exist){
                       if(ct.getDateFin().after(convertToCalendar(date)))
                           FichierController.getInstance().download(ct.getIdVideo()+".mp4");
                   }
               }
           }
           /*PERSISTENCE(SAUVEGARDE) DES CONTRATS*/
           FichierController.getInstance().sauverContratsADiffuser(contrat_remote);         
        } 
        catch (Exception e) {
            e.printStackTrace();
        } 
        finally {
            //finally block used to close resources
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
            }//end finally try
        }//end try
        
        return contrat_remote;
    }
    
    private Calendar convertToCalendar (Date date){
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        return cale;
    }
}