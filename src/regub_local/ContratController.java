/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub_local;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vinc
 */
public class ContratController {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://172.16.0.50:3306/regie_video";

    //  Database credentials
    static final String USER = "jdbcUser";
    static final String PASS = "jtankhull";

    public static void main(String[] args) {
        ArrayList<ContratModel> tcm_local = new ArrayList();
        FileInputStream fis;
        try {
            fis = new FileInputStream("contrats");
            ObjectInputStream in = new ObjectInputStream(fis);
            tcm_local = (ArrayList<ContratModel>) in.readObject();
            in.close();
            fis.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String sql = "SELECT * FROM Video INNER JOIN DiffusionsTypesRayons ON DiffusionsTypesRayons.idVideo=Video.idVideo INNER JOIN TypeRayon ON DiffusionsTypesRayons.idTypeRayon = TypeRayon.idTypeRayon WHERE TypeRayon.libelle = 'Charcuterie';";
            ResultSet rs = stmt.executeQuery(sql);

            ArrayList<ContratModel> tcm_remote = new ArrayList();
            boolean existe;
            Download dl = new Download();
            while (rs.next()) {
                existe = false;
                ContratModel cm = new ContratModel();
                cm.idVideo = rs.getInt("idVideo");
                cm.frequence = rs.getInt("frequence");
                cm.duree = rs.getInt("duree");
                cm.dateDebut = rs.getDate("dateDebut");
                cm.dateFin = rs.getDate("dateFin");
                tcm_remote.add(cm);

                Iterator<ContratModel> it = tcm_local.iterator();
                while (it.hasNext() && !(existe)) {
                    ContratModel cm_local = it.next();
                    if (cm_local.idVideo == cm.idVideo) {
                        tcm_local.remove(cm_local);
                        existe = true;
                    }
                }
                if (!(existe)) {
                    System.out.println("TELECHARGEMENT:" + cm.idVideo);
                    dl.getFile(Integer.toString(cm.idVideo));
                }
            }
            rs.close();

            Iterator<ContratModel> it = tcm_local.iterator();
            while (it.hasNext()) {
                ContratModel cm_local = it.next();
                System.out.println("FICHIER RESTANT:" + cm_local.idVideo);
            }

            try {
                FileOutputStream fos = new FileOutputStream("contrats");
                ObjectOutputStream out = new ObjectOutputStream(fos);
                out.writeObject(tcm_remote);
                out.close();
                fos.close();
                System.out.printf("Serialized data is saved in contrats");
            } catch (IOException i) {
                i.printStackTrace();
            }
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }
}
