package regub;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Configuration {
    
    private static Configuration INSTANCE; 
    
    private static final String FICHIER_CONFIGURATION = "config.properties";
    private final Properties properties;
    
    public static Configuration getInstance() throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new Configuration();
        }
        return INSTANCE;
    }
    
    private Configuration() throws IOException {
        properties = new Properties();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FICHIER_CONFIGURATION);
        
        if (inputStream != null) {
            properties.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + FICHIER_CONFIGURATION + "' not found in the classpath");
        }
    }
    
    public String getProp(String name) {
        return properties.getProperty(name);
    }
    
    public Calendar[] getHours(int jour) {
        Calendar heures[] = new Calendar[2];
        String chaine = "";
        String[] filtre;
        String[] ouverture;
        String[] fermeture;
        switch(jour) {
            case Calendar.MONDAY:
                chaine = this.getProp("lundi");
                break;
            case Calendar.TUESDAY:
                chaine = this.getProp("mardi");
                break;
            case Calendar.WEDNESDAY:
                chaine = this.getProp("mercredi");
                break;
            case Calendar.THURSDAY:
                chaine = this.getProp("jeudi");
                break;
            case Calendar.FRIDAY:
                chaine = this.getProp("vendredi");
                break;
            case Calendar.SATURDAY:
                chaine = this.getProp("samedi");
                break;
            case Calendar.SUNDAY:
                chaine = this.getProp("dimanche");
                break;
        }
        
        System.out.println(chaine);
        filtre = chaine.split("-");
        ouverture = filtre[0].split("h");
        fermeture = filtre[1].split("h");
        heures[0] = Calendar.getInstance();
        heures[1] = Calendar.getInstance();
        heures[0].set(Calendar.HOUR, Integer.parseInt(ouverture[0]));
        heures[0].set(Calendar.MINUTE, Integer.parseInt(ouverture[1]));
        heures[1].set(Calendar.HOUR, Integer.parseInt(fermeture[0]));
        heures[1].set(Calendar.MINUTE, Integer.parseInt(fermeture[1]));
        
        return heures;
    }
    
    public static void main(String[] args) {
        Configuration c = null;
        
        try {
            c = Configuration.getInstance();
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Calendar[] heures = c.getHours(Calendar.MONDAY);
        System.out.println("Heure d'ouverture : " + heures[0].get(Calendar.HOUR) + "h" + heures[0].get(Calendar.MINUTE));
        System.out.println("Heure de fermeture : " + heures[1].get(Calendar.HOUR) + "h" + heures[1].get(Calendar.MINUTE));
        
    }
}