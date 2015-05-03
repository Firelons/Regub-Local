package regub;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Properties;

public class Configuration {
    
    private static Configuration INSTANCE; 
    
    private static final String FICHIER_CONFIGURATION = "config.properties";
    private static final String REGEX_HORAIRES = "^[0-9]{1,2}h[0-9]{1,2}-[0-9]{1,2}h[0-9]{1,2}$";
    
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
    
    public Calendar[] getHoraires(int jour) throws Exception {
        Calendar horaires[] = new Calendar[2];
        String st;
        String[] filtre, horaire_ouverture, horaire_fermeture;
        
        switch(jour) {
            case Calendar.MONDAY:
                st = this.getProp("lundi"); break;
            case Calendar.TUESDAY:
                st = this.getProp("mardi"); break;
            case Calendar.WEDNESDAY:
                st = this.getProp("mercredi"); break;
            case Calendar.THURSDAY:
                st = this.getProp("jeudi"); break;
            case Calendar.FRIDAY:
                st = this.getProp("vendredi"); break;
            case Calendar.SATURDAY:
                st = this.getProp("samedi"); break;
            case Calendar.SUNDAY:
                st = this.getProp("dimanche"); break;
            default:
                throw new IllegalArgumentException("Jour inconnu.");
        }
        
        if (!st.matches(REGEX_HORAIRES)) throw new Exception("Les horaires n'ont pas été configurés correctement.");

        filtre = st.split("-");
        horaire_ouverture = filtre[0].split("h");
        horaire_fermeture = filtre[1].split("h");
        horaires[0] = Calendar.getInstance();
        horaires[1] = Calendar.getInstance();
        horaires[0].set(Calendar.HOUR_OF_DAY, Integer.parseInt(horaire_ouverture[0]));
        horaires[0].set(Calendar.MINUTE, Integer.parseInt(horaire_ouverture[1]));
        horaires[0].set(Calendar.SECOND, 0);
        horaires[1].set(Calendar.HOUR_OF_DAY, Integer.parseInt(horaire_fermeture[0]));
        horaires[1].set(Calendar.MINUTE, Integer.parseInt(horaire_fermeture[1]));
        horaires[1].set(Calendar.SECOND, 0);
        
        return horaires;
    }
}