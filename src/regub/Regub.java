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
        
        /** TEST SI TOUTES LES VIDEOS EXISTENT **/
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
        
        /** GENERATION D'UNE PLAYLIST A PARTIR DE LA LISTE DE CONTRATS, DE L'HEURE ACTUELLE ET DE L'HEURE DE FERMETURE **/
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
