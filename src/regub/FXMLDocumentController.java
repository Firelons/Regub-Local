/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author paul
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private BorderPane bp;

    @FXML
    private Label label;

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
    
    @FXML
    private Pane pane;
    
    private StackPane spane;

    Playlist p;
    Stage primaryStage = new Stage();
    HashMap<String, MediaPlayer> listeMediaPlayer;
    int position = 0;
    int _width = 300;
    int _height = 300;
    Scene scene1;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @FXML
    private void handleButtonEscape(KeyEvent event) {
        System.out.println("KEYEVENT");
        if (event.getCode() == KeyCode.ESCAPE) {
            System.out.println("ESCAPE");
            Regub.stage_access.setScene(Regub.scene_access);
            /*x.setValue(0);
            y.setValue(0);*/
        }
    }

    @FXML
    private void handleButtonPleinEcranAction(ActionEvent event) throws InterruptedException {
        /*primaryStage.setFullScreen(true);
         primaryStage.requestFocus();*/
        //Regub.stage_access.setScene(scene);
        /*bp.getChildren().remove(0);
        bp.getChildren().remove(1);
        System.out.println("PLEIN ECRAN");
        pane.getChildren().add(mv);
        
        final DoubleProperty width = mv.fitWidthProperty();
        final DoubleProperty height = mv.fitHeightProperty();

        width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));

        Regub.stage_access.setFullScreen(true);*/
        spane.getChildren().add(mv);
        Regub.stage_access.setScene(scene1);
    }

    private void remplirListe() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 0; i < p.getOrdreDiffusion().length; i++) {
            Contrat c = p.getContrat(p.getOrdreDiffusion()[i]);
            items.add("#" + Integer.toString(i + 1) + " | id : " + c.getIdVideo() + " | titre : " + c.getTitre());
        }
        liste.setItems(items);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //final MediaView mv1 = new MediaView();

        //récupération de la playlist du jour
        p = PlaylistController.getInstance().getPlaylist();

        //création de la liste de mediaplayer
        listeMediaPlayer = new HashMap<>();
        for (Contrat c : p.getListeContrats()) {
            Media m = new Media(new File("videos/" + c.getIdVideo() + ".mp4").toURI().toString());
            MediaPlayer mp = new MediaPlayer(m);
            mp.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    // actions here e.g.:
                    MediaPlayer mp = listeMediaPlayer.get("pause");
                    mp.seek(Duration.ZERO);
                    //mv1.setMediaPlayer(mp);
                    mv.setMediaPlayer(mp);
                    mp.play();
                    Regub.stage_access.setScene(Regub.scene_access);
                    bp.setCenter(mv);
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
        //mv1.setMediaPlayer(premierMediaPlayer);
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
                //mv1.setMediaPlayer(premierMediaPlayer);
                mv.setMediaPlayer(premierMediaPlayer);
                premierMediaPlayer.play();

            }
        }, p.getDureeEntreVideos() * 1000, p.getDureeEntreVideos() * 1000);
        mv.setOpacity(0.5);
        mv.setPreserveRatio(false);
        /*final DoubleProperty width = mv.fitWidthProperty();
         final DoubleProperty height = mv.fitHeightProperty();
         // final DoubleProperty height = mv.fitHeightProperty();

         width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
         height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));

         mv.setPreserveRatio(false);
        */
         spane = new StackPane();
         //root.getChildren().add(mv);

        scene1 = new Scene(spane);
              
         /*primaryStage.initStyle(StageStyle.UNDECORATED);
         primaryStage.setScene(scene);
         primaryStage.setTitle("Full Screen Video Player");
         primaryStage.show();*/
    }
}
