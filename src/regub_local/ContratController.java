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
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
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
        ArrayList<ContratModel> tcm_local;
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

            String sql = "SELECT * FROM Video INNER JOIN DiffusionsTypesRayons INNER JOIN TypeRayon WHERE TypeRayon.libelle = 'Poissonerie'";
            ResultSet rs = stmt.executeQuery(sql);

            ArrayList<ContratModel> tcm_remote = new ArrayList();

            while (rs.next()) {
                ContratModel cm = new ContratModel();
                cm.idVideo = rs.getInt("idVideo");
                cm.frequence = rs.getInt("frequence");
                cm.duree = rs.getInt("duree");
                cm.dateDebut = rs.getDate("dateDebut");
                cm.dateFin = rs.getDate("dateFin");
                tcm_remote.add(cm);
            }
            rs.close();

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
