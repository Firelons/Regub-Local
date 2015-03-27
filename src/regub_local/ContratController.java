package regub_local;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author vinc
 */
public class ContratController {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://172.16.0.50:3306/regie_video";

    static final String USER = "jdbcUser";
    static final String PASS = "jtankhull";
    private static ContratController INSTANCE;

    private ContratController() {
    }

    public static ContratController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ContratController();
        }
        return INSTANCE;
    }

    public ArrayList<ContratModel> getContrat(String rayon) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ArrayList<ContratModel> contrat_remote = new ArrayList();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "SELECT * FROM Video INNER JOIN DiffusionsTypesRayons ON DiffusionsTypesRayons.idVideo=Video.idVideo INNER JOIN TypeRayon ON DiffusionsTypesRayons.idTypeRayon = TypeRayon.idTypeRayon WHERE TypeRayon.libelle = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, rayon);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                ContratModel cm = new ContratModel();
                cm.idVideo = rs.getInt("idVideo");
                cm.frequence = rs.getInt("frequence");
                cm.duree = rs.getInt("duree");
                cm.dateDebut = rs.getDate("dateDebut");
                cm.dateFin = rs.getDate("dateFin");
                contrat_remote.add(cm);
                System.out.println("TEST:"+cm.idVideo);
            }
            rs.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
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
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("TEST 1 2 3");
        return contrat_remote;
    }
}
