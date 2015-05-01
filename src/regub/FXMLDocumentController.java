/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author paul
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private BorderPane bp;

    @FXML
    private Label lTimer;
    
    @FXML
    private Label lHoraires;

    @FXML
    private AnchorPane anchor;

    @FXML
    private Button bPleinEcran;

    @FXML
    private Button bQuitter;

    @FXML
    private MediaView mv;

    @FXML
    private ListView<String> liste;
    
    private ArrayList<Contrat> contrats_a_diffuser;
    
    private StackPane sp;

    
    Playlist p;
    Stage primaryStage = new Stage();
    HashMap<String, MediaPlayer> listeMediaPlayer;
    int position = 0;
    int _width = 300;
    int _height = 300;
    Scene scene_plein_ecran;
    
    void activerModePleinEcran() {
        final DoubleProperty width = mv.fitWidthProperty();
        final DoubleProperty height = mv.fitHeightProperty();
        width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));

        sp.getChildren().add(mv);
        Regub.stage.setScene(scene_plein_ecran);
        Regub.stage.setFullScreen(true);
    }
    
    void desactiverModePleinEcran() {
        final DoubleProperty width = mv.fitWidthProperty();
        final DoubleProperty height = mv.fitHeightProperty();
        width.unbind();
        height.unbind();
        mv.fitHeightProperty().set(200);
        mv.fitWidthProperty().set(400);
        
        bp.setCenter(mv);
        Regub.stage.setScene(Regub.scene_principale);
        Regub.stage.setFullScreen(false);  
    }
    
    @FXML
    private void handleButtonPleinEcranAction(ActionEvent event) throws InterruptedException {
        activerModePleinEcran();
    }

    private void remplirListe() {
        ObservableList<String> items = FXCollections.observableArrayList();
        
        StringBuilder sb;
       
        for (Diffusion d : p.getListeDiffusion()) {
            sb = new StringBuilder();
            sb.append("(");
            sb.append(String.format("%02d", d.getHeureDiffusion().get(Calendar.HOUR_OF_DAY)));
            sb.append(":");
            sb.append(String.format("%02d", d.getHeureDiffusion().get(Calendar.MINUTE)));
            sb.append(":");
            sb.append(String.format("%02d", d.getHeureDiffusion().get(Calendar.SECOND)));
            sb.append(") ");
            sb.append(d.getContrat().getTitre());
            items.add(sb.toString());
        }
        liste.setItems(items);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //SI DATE ACTUELLE > DATE DE FERMETURE DE LA JOURNEE : AFFICHER MESSAGE ERREUR ET QUITTER APPLI
        /* if... */
        
        /* TRUC SUR A 100 % */
            //horaires du jour
            Calendar[] horaires = null;
            try {
               horaires = Configuration.getInstance().getHours(Calendar.DAY_OF_WEEK); 
            } catch (IOException ex) {
               Logger.getLogger(PlaylistController.class.getName()).log(Level.SEVERE, null, ex);
            }
            String[] days = new DateFormatSymbols(Locale.getDefault()).getWeekdays();
            StringBuilder sb = new StringBuilder();
            
            sb.append(days[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)].toUpperCase()).append(" : (");
            sb.append(String.format("%02d", horaires[0].get(Calendar.HOUR_OF_DAY))).append("h");
            sb.append(String.format("%02d", horaires[0].get(Calendar.MINUTE))).append(" - ");
            sb.append(String.format("%02d", horaires[1].get(Calendar.HOUR_OF_DAY))).append("h");
            sb.append(String.format("%02d", horaires[1].get(Calendar.MINUTE))).append(")");
        
            lHoraires.setText(sb.toString());
        /* */
        
        contrats_a_diffuser = new ArrayList();
        FileInputStream fis;
        try {
            fis = new FileInputStream("contrats");
            try (ObjectInputStream in = new ObjectInputStream(fis)) {
                contrats_a_diffuser = (ArrayList<Contrat>) in.readObject();
                in.close();
            }
            fis.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Contrat local non existant...");
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       

        try {
            p = new Playlist(Calendar.getInstance(), horaires[1], contrats_a_diffuser);
        } catch (RegubException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //récupération de la playlist du jour
        
        lHoraires.setText(sb.toString());
        
        mv.setPreserveRatio(false);

        //création de la liste de mediaplayer
        listeMediaPlayer = new HashMap<>();
        for (Contrat c : p.getListeContrats()) {
            Media m = new Media(new File("videos/" + c.getIdVideo() + ".mp4").toURI().toString());
            MediaPlayer mp = new MediaPlayer(m);
            mp.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    MediaPlayer mp = listeMediaPlayer.get("pause");
                    mp.seek(Duration.ZERO);
                    mv.setMediaPlayer(mp);
                    mp.play();
                }
            });
            listeMediaPlayer.put(Integer.toString(c.getIdVideo()), mp);
        }
        Media m = new Media(new File("videos/pause.mp4").toURI().toString());
        MediaPlayer mp = new MediaPlayer(m);
        mp.setCycleCount(MediaPlayer.INDEFINITE);
        listeMediaPlayer.put("pause", mp);

        //remplissage de la listeview
        remplirListe();

        //lancer la playlist
        MediaPlayer premierMediaPlayer = listeMediaPlayer.get("pause");
        mv.setMediaPlayer(premierMediaPlayer);
        premierMediaPlayer.play();

      
                
                
        
        
        
        ArrayList<Timer> toto = new ArrayList<Timer>();
        
        for (Diffusion d : p.getListeDiffusion()) {
            toto.add(new Timer());
        }
        
        int o=0;
        for (Diffusion d : p.getListeDiffusion()) {
            
            toto.get(o).schedule(new TimerTask() {
                @Override
                public void run() {
                    listeMediaPlayer.get("pause").stop();
                    listeMediaPlayer.get("pause").seek(Duration.ZERO);
                    MediaPlayer premierMediaPlayer = listeMediaPlayer.get(Integer.toString(p.getOrdreDiffusion()[position]));

                    position++;
                    premierMediaPlayer.seek(Duration.ZERO);
                    mv.setMediaPlayer(premierMediaPlayer);
                    premierMediaPlayer.play();

                }
            }, d.getHeureDiffusion().getTime());
            o++;
        }

        
        sp = new StackPane();
        scene_plein_ecran = new Scene(sp);
        scene_plein_ecran.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    desactiverModePleinEcran();
                }
            }
          });
    }
}
