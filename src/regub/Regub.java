/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author paul
 */
public class Regub extends Application {
    
    static Stage stage;
    static Scene scene_principale;
    static Calendar[] horaires;
    static ArrayList<Contrat> contrats_a_diffuser;
    static Playlist playlist;
    
    @Override
    public void start(Stage stage) throws IOException {
        
        /** TEST SI LES HORAIRES EXISTENT **/
        try { 
            horaires = Configuration.getInstance().getHoraires(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        } catch (Exception ex) {
            afficherMessageErreur(ex.getMessage());
            return;
        }
        
        /** TEST SI LE MAGASIN EST DEJA FERME **/
        if (horaires[1].getTime().before(Calendar.getInstance().getTime())) {
            afficherMessageErreur("Le magasin est fermé pour ajourd'hui.");
            return;
        }
        
        /** RECUPERATION DES CONTRATS DU JOUR **/
        contrats_a_diffuser = ContratController.getInstance().getContratsADiffuser();
        
        /** TEST SI IL Y A ENCORE DES CONTRATS A DIFFUSER AUJOURDHUI **/
        if (contrats_a_diffuser.isEmpty()) {
            afficherMessageErreur("Il n'y a plus de contrat à diffuser pour aujourd'hui.");
            return;
        }
        
        /** TEST SI TOUTES LES VIDEOS DE CONTRATS EXISTENT **/
        for (Contrat contrat : contrats_a_diffuser) {
            File f = new File("videos/"+contrat.getIdVideo()+".mp4");
            if(!f.exists() || f.isDirectory()) {
                afficherMessageErreur("La vidéo pour le contrat n°" + contrat.getIdVideo() + " est introuvable.");
                return;
            }
        }
        
        /** TEST SI LA VIDEO DE PAUSE EXISTE **/
        File f = new File("videos/pause.mp4");
        if(!f.exists() || f.isDirectory()) {
            afficherMessageErreur("La vidéo de pause est introuvable.");
            return;
        }
        
        /** TANT QU'ON A PAS LE TEMPS DE DIFFUSER TOUTES LES VIDEOS, ON REPORTE CERTAINE DIFFUSION A DEMAIN **/
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
            
            somme_durees = somme_durees + (somme_frequences+1)*Integer.parseInt(Configuration.getInstance().getProp("duree_minimum_pause"));
            
            if (somme_durees > duree_diffusion) {
                Contrat contrat_tard = contrats_a_diffuser.get(0);
                for (Contrat contrat : contrats_a_diffuser) {
                    if (contrat.getDateFin().getTime().after(contrat_tard.getDateFin().getTime())) {
                        contrat_tard = contrat;
                    }
                }
                contrat_tard.setFrequence(contrat_tard.getFrequence()-1);
                if (contrat_tard.getFrequence() == 0) {
                    contrats_a_diffuser.remove(contrat_tard);
                }
            }
            
        } while(somme_durees > duree_diffusion); 
        
        
        /** TENTATIVE DE GENERATION D'UNE PLAYLIST A PARTIR DE LA LISTE DE CONTRATS,
             * DE L'HEURE ACTUELLE ET DE L'HEURE DE FERMETURE **/
        playlist = new Playlist(Calendar.getInstance(), Regub.horaires[1], contrats_a_diffuser);
        
        
        
        
        
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Regub.scene_principale = new Scene(root);
        Regub.stage = stage;
        Regub.stage.setScene(scene_principale);
        Regub.stage.setFullScreenExitHint("");
        Regub.stage.setResizable(false);
        Regub.stage.setAlwaysOnTop(true);
        Regub.stage.initStyle(StageStyle.UTILITY);
        Regub.stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public void afficherMessageErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
    
}
