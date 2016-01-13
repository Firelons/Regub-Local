
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
public final class EnregistrerDiffusions  {
       
     private static String JDBC_DRIVER; 
    private String DB_URL ;
    private  String USER;
    private String PASS;
    private String idmagasin;
    private String idTypeRayon;
 
     String[] dat = null;
        int[] id = null;
        
public EnregistrerDiffusions(){
     Configuration conf;
        try{
            conf = Configuration.getInstance();
            JDBC_DRIVER = conf.getProp("jdbc_driver");
            DB_URL = conf.getProp("db_url");
            USER = conf.getProp("user");
            PASS = conf.getProp("pass");
            idmagasin = conf.getProp("idmagasin");
            idTypeRayon = conf.getProp("idtyperayon");
         try {
             upload();
         } catch (ClassNotFoundException ex) {
             Logger.getLogger(EnregistrerDiffusions.class.getName()).log(Level.SEVERE, null, ex);
         } catch (SQLException ex) {
             Logger.getLogger(EnregistrerDiffusions.class.getName()).log(Level.SEVERE, null, ex);
         }
         }
         catch(RegubException ex)
         {
         Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE,null,ex);
         }
    }

 public void upload()throws ClassNotFoundException, SQLException {
      String ligne;
         String lign;
          int numLigne = 0; 
            int l =0;
          
            
        StringBuilder sb = new StringBuilder();
        Calendar cal = Calendar.getInstance();
        sb.append(String.format("%02d", cal.get(Calendar.YEAR))).append("-")
                .append(String.format("%02d", cal.get(Calendar.MONTH)+1)).append("-")
                .append(String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)));
      
        try {
         
          InputStream ips = new FileInputStream("logs/diffusions/"+sb+".txt");
          InputStream ips1 = new FileInputStream("logs/diffusions/"+sb+".txt");
          InputStreamReader ipsr = new InputStreamReader(ips);
           InputStreamReader ipsr1 = new InputStreamReader(ips1);
          BufferedReader count = new BufferedReader(ipsr);
       BufferedReader in = new BufferedReader(ipsr1);
           
       //compter le nombre de ligne afin de creer le tableau
         // par exemple 
        while ((lign = count.readLine()) != null) 
        { l++; }
        count.close();
        
        // creer les tableaux id et dat pour idvideo et datediffusion
        dat = new String[l];
        id = new int[l];
        int i =0;
        // Remplir les tableaux a l'aide des fichiers
        while ((ligne = in.readLine()) != null) 
        { 
        numLigne++; 
        System.out.println(ligne);                                                    
        int ind = ligne.length();                             // pour recuperer la fin de ligne
        dat[numLigne -1] = ligne.substring(0, 19); 
        id[numLigne -1] = Integer.parseInt(ligne.substring(20,ind));    
        } 
            in.close();       
    }
    catch (IOException ie)
    {
         ie.printStackTrace(); 
    } 
        // Connection puis enregistrement des diffusions dans la BD
        Connection conn = null;
        //PreparedStatement ps = null;
        PreparedStatement ps = null;
           
        try {
             Class.forName(JDBC_DRIVER);
             conn = DriverManager.getConnection(DB_URL, USER, PASS);
    
            String sql = "INSERT INTO Diffusions VALUES(NULL,?,?,?,?)";
            ps = conn.prepareStatement(sql);
        
            for(int j = 0;j<dat.length;j++){
   
            ps.setInt(1,id[j]);
            ps.setInt(2,Integer.parseInt(idmagasin));
            ps.setInt(3,Integer.parseInt(idTypeRayon));
            ps.setString(4,dat[j]);
            System.out.println(ps.toString());
//            ps.setObject(2, magasin,Types.VARCHAR);
//              ps.setObject(3, rayon,Types.VARCHAR);
            ps.executeUpdate();
            System.out.println("Enregistrement...");
           
         }
               
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }// do nothing
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        
        
    }
 
 public static void main(String[] args) {
      new EnregistrerDiffusions();
    }
}

