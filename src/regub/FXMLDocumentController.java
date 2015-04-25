/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub;

import java.io.File;
import static java.lang.Math.round;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
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
import static javafx.scene.input.KeyCode.R;
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
        int dev = p.getDureeEntreVideos();
        Calendar heures = Calendar.getInstance();
        heures.set(Calendar.HOUR, round(dev));
        heures.set(Calendar.MINUTE, dev-round(dev));
        
        for (int i = 0; i < p.getOrdreDiffusion().length; i++) {
            Contrat c = p.getContrat(p.getOrdreDiffusion()[i]);
            items.add(heures.get(Calendar.HOUR)+":"+heures.get(Calendar.MINUTE) + "#" + Integer.toString(i + 1) + " | id : " + c.getIdVideo() + " | titre : " + c.getTitre());
        }
        liste.setItems(items);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //récupération de la playlist du jour
        p = PlaylistController.getInstance().getPlaylist();
        String[] days = new DateFormatSymbols(Locale.getDefault()).getWeekdays();
        StringBuilder sb = new StringBuilder();
        sb.append(days[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)].toUpperCase()).append(" : ");
        sb.append("( ").append(String.format("%02d", p.getHeureDebutDiffusion().get(Calendar.HOUR_OF_DAY))).append("h");
        sb.append(String.format("%02d", p.getHeureDebutDiffusion().get(Calendar.MINUTE))).append(" - ");
        sb.append(String.format("%02d", p.getHeureFinDiffusion().get(Calendar.HOUR_OF_DAY))).append("h");
        sb.append(String.format("%02d", p.getHeureFinDiffusion().get(Calendar.MINUTE))).append(" )");
        
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

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                listeMediaPlayer.get("pause").stop();
                listeMediaPlayer.get("pause").seek(Duration.ZERO);
                MediaPlayer premierMediaPlayer = listeMediaPlayer.get(Integer.toString(p.getOrdreDiffusion()[position]));

                position++;
                if (position >= p.getOrdreDiffusion().length) {
                    timer.cancel();
                }
                premierMediaPlayer.seek(Duration.ZERO);
                mv.setMediaPlayer(premierMediaPlayer);
                premierMediaPlayer.play();

            }
        }, p.getDureeEntreVideos() * 1000, p.getDureeEntreVideos() * 1000);

        
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
