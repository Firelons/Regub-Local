
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Regub extends Application {
    
    static Stage stage;
    static Scene scene_principale;
    static Calendar[] horaires;
    static ArrayList<Contrat> contrats_a_diffuser;
    static Playlist playlist;
    static Configuration config;
    
    @Override
    public void start(Stage stage) {
        
        FichierController.loguer_systeme("Application de diffusion : initialisation");
        
        /** TEST SI LA VIDEO DE PAUSE EXISTE **/
        File f = new File("videos/pause.mp4");
        if(!f.exists() || f.isDirectory()) {
            FichierController.loguer_systeme("\tLa vidéo de pause est introuvable.");
            terminer();
        }
        
        /** RECUPERATION DU FICHIER DE CONFIGURATION **/
        try {
            config = Configuration.getInstance();
        } catch (RegubException ex) {
            FichierController.loguer_systeme("\t"+ex.getMessage());
            terminer();
        }
        
        /** TEST SI LES HORAIRES EXISTENT ET SONT BIEN CONFIGURES **/
        try { 
            horaires = config.getHoraires(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        } catch (RegubException ex) {
            FichierController.loguer_systeme("\t"+ex.getMessage());
            terminer();
        }
        
        /** TEST SI LE MAGASIN EST OUVERT **/
        if (horaires[1].getTime().before(Calendar.getInstance().getTime()) || horaires[0].getTime().after(Calendar.getInstance().getTime()) ) {
            FichierController.loguer_systeme("\tLe magasin est fermé.");
            terminer();
        }
        
        /** RECUPERATION DES CONTRATS DU JOUR **/
        try {
            contrats_a_diffuser = FichierController.getInstance().chargerContratsADiffuser();
        } catch (RegubException ex) {
            FichierController.loguer_systeme("\t"+ex.getMessage());
            terminer();
        }
        
        /** AJOUTER LES CONTRATS REPORTES AUX CONTRATS A DIFFUSER **/
        ArrayList<Contrat> contrats_reportes = FichierController.getInstance().chargerContratsReportes();
        if (!contrats_reportes.isEmpty()) {
            contrats_reportes.stream().forEach((cr) -> {
                contrats_a_diffuser.stream().filter((cad) -> (cad.getIdVideo() == cr.getIdVideo())).forEach((cad) -> {
                    cad.setFrequence(cad.getFrequence()+cr.getFrequence());
                });
            });
        }
        contrats_reportes.removeAll(contrats_reportes);
        
        /** TEST SI TOUTES LES VIDEOS DE CONTRATS EXISTENT, REPORT SI NON **/
        for (int i = contrats_a_diffuser.size()-1; i >= 0; i--) {
            f = new File("videos/"+contrats_a_diffuser.get(i).getIdVideo()+".mp4");
            if(!f.exists() || f.isDirectory()) {
                FichierController.loguer_systeme("\tLa vidéo pour le contrat n°" + contrats_a_diffuser.get(i).getIdVideo() + " est introuvable. Contrat reporté.");
                contrats_reportes.add(new Contrat(contrats_a_diffuser.get(i)));
                contrats_a_diffuser.remove(contrats_a_diffuser.get(i));     
            }
        }
        
        /** TANT QU'ON A PAS LE TEMPS DE DIFFUSER TOUTES LES VIDEOS, ON REPORTE CERTAINE DIFFUSION **/
        int duree_diffusion = (int)((Regub.horaires[1].getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / 1000);
        int somme_durees;
        int somme_frequences;
        
        do {
            somme_durees = 0;
            somme_frequences = 0;
            for (Contrat c : contrats_a_diffuser) {
                somme_durees = somme_durees + (c.getFrequence() * c.getDuree());
                somme_frequences = somme_frequences + c.getFrequence();
            }
            
            somme_durees = somme_durees + (somme_frequences+1)*Integer.parseInt(config.getProp("duree_minimum_pause"));
            
            if (somme_durees > duree_diffusion) {
                Contrat contrat_tard = contrats_a_diffuser.get(0);
                for (Contrat c : contrats_a_diffuser) {
                    if (c.getDateFin().getTime().after(contrat_tard.getDateFin().getTime())) {
                        contrat_tard = c;
                    }
                }
                contrat_tard.setFrequence(contrat_tard.getFrequence()-1);
                if (contrat_tard.getFrequence() == 0) {
                    contrats_a_diffuser.remove(contrat_tard);
                }
                
                boolean reporte = false;
                for (Contrat c : contrats_reportes) {
                    if (c.getIdVideo() == contrat_tard.getIdVideo()) {
                        c.setFrequence(c.getFrequence()+1);
                        reporte = true;
                    }
                }
                if (!reporte) {
                    Contrat c = new Contrat(contrat_tard);
                    c.setFrequence(1);
                    contrats_reportes.add(c);
                }
            }
            
        } while(somme_durees > duree_diffusion); 
        
        
        
        /** GENERATION D'UNE PLAYLIST A PARTIR DE LA LISTE DE CONTRATS,
             * DE L'HEURE ACTUELLE ET DE L'HEURE DE FERMETURE **/
        playlist = new Playlist(Calendar.getInstance(), Regub.horaires[1], contrats_a_diffuser);
        
        try {
            FichierController.getInstance().sauverContratsADiffuser(contrats_a_diffuser);
            FichierController.getInstance().sauverContratsReportes(contrats_reportes);
        } catch (RegubException ex) {
            FichierController.loguer_systeme("\t"+ex.getMessage());
            terminer();
        }
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        
        Regub.stage = stage;
        Parent root = null;
        try {
            root = fxmlLoader.load(getClass().getResource("DiffusionIHM.fxml").openStream());
        } catch (IOException ex) {
            Logger.getLogger(Regub.class.getName()).log(Level.SEVERE, null, ex);
        }
        DiffusionIHMController controller = (DiffusionIHMController) fxmlLoader.getController();
        Regub.scene_principale = new Scene(root);
        Regub.stage.setScene(scene_principale);
        Regub.stage.setFullScreenExitHint("");
        Regub.stage.setResizable(false);
        Regub.stage.show();
        controller.activerModePleinEcran();
        FichierController.loguer_systeme("Application de diffusion : en cours");
    }

    public static void terminer() {
        FichierController.loguer_systeme("Application de diffusion : fin");
        Runtime.getRuntime().halt(0);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
