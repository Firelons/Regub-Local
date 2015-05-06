
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
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://172.16.0.50:3306/regie_video";
    private static final String USER = "jdbcUser";
    private static final String PASS = "jtankhull";
    
    private static ContratController INSTANCE;

    public static ContratController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ContratController();
        }
        return INSTANCE;
    }

    public ArrayList<Contrat> telechargerContrats(String rayon) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ArrayList<Contrat> contrat_remote = new ArrayList();

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "SELECT * FROM Video INNER JOIN DiffusionsTypesRayons ON DiffusionsTypesRayons.idVideo=Video.idVideo INNER JOIN TypeRayon ON DiffusionsTypesRayons.idTypeRayon = TypeRayon.idTypeRayon WHERE TypeRayon.libelle = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, rayon);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                /*Contrat cm = new Contrat(rs.getInt("idVideo"), rs.getString("titre"), rs.getInt("frequence"), 
                        rs.getInt("duree"), rs.getDate("dateDebut"), rs.getDate("dateFin"));*/
            }
            rs.close();
        } catch (SQLException | ClassNotFoundException se) {
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
            }//end finally try
        }//end try
        
        return contrat_remote;
    }
}