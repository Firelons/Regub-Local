
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Landry Emmanuel
 */
public class jdbc {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/regub";
    private static final String USER = "root";
    private static final String PASS = "";
    private static final String DOSSIER = "test";

    
    private static  ArrayList<video> listvideobd = new ArrayList<>();
    
    public static void main(String[] args) {
        //System.out.println("avant le try!");
        //Testing sql request
         Connection conn = null;
         
         
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            /*String sql = "SELECT * FROM Video INNER JOIN DiffusionsTypesRayons ON DiffusionsTypesRayons.idVideo=Video.idVideo INNER JOIN TypeRayon ON DiffusionsTypesRayons.idTypeRayon = TypeRayon.idTypeRayon WHERE TypeRayon.libelle = ?";
            preparedStatement.setString(1, rayon);
            preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
*/
            
            String sql = "SELECT * FROM video;"; 
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            
            while (rs.next()) {
                /*Contrat cm = new Contrat(rs.getInt("idVideo"), rs.getString("titre"), rs.getInt("frequence"), 
                        rs.getInt("duree"), rs.getDate("dateDebut"), rs.getDate("dateFin"));*/
                int idVideo = rs.getInt("idVideo");
                String titre = rs.getString("titre") ;
                int frequence = rs.getInt("frequence");
                int duree = rs.getInt("duree");
                Date dateDebut = rs.getDate("dateDebut");
                Date dateFin = rs.getDate("dateFin");
                Date dateReception = rs.getDate("dateReception");
                Date dateValidation = rs.getDate("dateValidation");
                int tarif = rs.getInt("tarif");
                int statut = rs.getInt("statut");
                int idCommercial = rs.getInt("idCommercial");
                int idClient = rs.getInt("idClient");
                
                video mavideo = new video(idVideo, titre, frequence, duree, dateDebut, dateFin, dateReception, dateValidation, tarif, statut, idCommercial, idClient);
                listvideobd.add(mavideo);
               // ajouter_info_video(mavideo);
                /*File f = new File ("textfile"); 
                try
                {
                    FileReader fr = new FileReader (f);
                }
                catch (FileNotFoundException exception)
                {
                    System.out.println ("Le fichier n'a pas été trouvé");
                }*/
                
                /*System.out.println(""+rs.getInt("idVideo"));
                System.out.println(rs.getString("titre"));*/
                //System.out.println(mavideo.getTitre());
            }
            rs.close();
            ajouter_fichier_video(listvideobd);
            //Lecture des contrats
            
            
        } catch (SQLException | ClassNotFoundException se) {
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Regub.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void ajouter_info_video(video nomvideo) throws IOException {
        
        StringBuilder sb = new StringBuilder();
        sb.append(DOSSIER).append("/")
                .append(nomvideo.getTitre()).append("_")
                .append(nomvideo.getIdVideo())                
                .append(".txt");
        try (BufferedWriter output = new BufferedWriter(new FileWriter(sb.toString(), true))) {
            sb = new StringBuilder();
            sb.append(nomvideo.getIdVideo());
            output.append(sb.toString());
            output.newLine();
            
            sb = new StringBuilder();
            sb.append(nomvideo.getTitre());
            output.append(sb.toString());
            output.newLine();
            
            sb = new StringBuilder();
            sb.append(nomvideo.getFrequence());
            output.append(sb.toString());
            output.newLine();
            
            sb = new StringBuilder();
            sb.append(nomvideo.getDuree());
            output.append(sb.toString());
            output.newLine();
            
            sb = new StringBuilder();
            sb.append(nomvideo.getDateDebut());
            output.append(sb.toString());
            output.newLine();
            
            sb = new StringBuilder();
            sb.append(nomvideo.getDateFin());
            output.append(sb.toString());
            output.newLine();
            
            sb = new StringBuilder();
            sb.append(nomvideo.getDateReception());
            output.append(sb.toString());
            output.newLine();
            
            sb = new StringBuilder();
            sb.append(nomvideo.getDateValidation());
            output.append(sb.toString());
            output.newLine();
            
            sb = new StringBuilder();
            sb.append(nomvideo.getTarif());
            output.append(sb.toString());
            output.newLine();
            
            sb = new StringBuilder();
            sb.append(nomvideo.getStatut());
            output.append(sb.toString());
            output.newLine();
            
            sb = new StringBuilder();
            sb.append(nomvideo.getIdMagasin());
            output.append(sb.toString());
            output.newLine();
            
            sb = new StringBuilder();
            sb.append(nomvideo.getIdClient());
            output.append(sb.toString());
            output.newLine();
        }
    }
    
    public static void ajouter_fichier_video(ArrayList<video> listvideobd) {
        for( video vid : listvideobd){
            File dir = new File(DOSSIER+"/");
                File[] files = dir.listFiles();
                for(File file : files) file.delete();
                        
        }
        
        for( video vid : listvideobd){
        try {
                ajouter_info_video(vid);
            } catch (IOException ex) {
                Logger.getLogger(jdbc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    
}
