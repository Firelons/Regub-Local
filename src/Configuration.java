
import java.io.FileInputStream;
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
    
    public static Configuration getInstance() throws RegubException {
        if (INSTANCE == null) {
            INSTANCE = new Configuration();
        }
        return INSTANCE;
    }
    
    private Configuration() throws RegubException {
        properties = new Properties();

        InputStream inputStream;
        try {
            inputStream = new FileInputStream("fichiers/"+FICHIER_CONFIGURATION);
            properties.load(inputStream); 
        } catch (FileNotFoundException ex) {
            throw new RegubException("Le fichier de configuration est introuvable.");
        } catch (IOException ex) {
            throw new RegubException("Erreur lors de la lecture du fichier de configuration.");
        } 
    }
    
    public String getProp(String name) {
        return properties.getProperty(name);
    }
    
    public int getDureeMinimumPause() throws RegubException {
        
        int duree_minimum_pause;
        try {
            duree_minimum_pause = Integer.parseInt(getProp("duree_minimum_pause"));
            if (duree_minimum_pause > 0) {
                return duree_minimum_pause;
            } else {
                throw new RegubException("La durée minimale de la pause n'a pas été configuré correctement.");
            }
        } catch (NumberFormatException ex) {
            throw new RegubException("La durée minimale de la pause n'a pas été configuré correctement.");
        }
    }
    
    public Calendar[] getHoraires(int jour) throws RegubException {
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
                throw new RegubException("Erreur lors de la récupération des horaires.");
        }
        
        if (!st.matches(REGEX_HORAIRES)) throw new RegubException("Les horaires n'ont pas été configurés correctement.");

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
        
        if (horaires[0].getTime().after(horaires[1].getTime())) throw new RegubException("Les horaires n'ont pas été configurés correctement.");
        
        return horaires;
    }
}