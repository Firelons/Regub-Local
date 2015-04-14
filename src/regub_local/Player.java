/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub_local;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author vinc
 */
public class Player {
    private Stage stage;
    List<MediaPlayer> players = new ArrayList<>();
    List<MediaPlayer> players_wait = new ArrayList<>();
    int position = 0;
    MediaView mv = new MediaView();
    
    public Player(Stage stage) {
        this.stage = stage;
        
    }
    
    public void chargerPlaylist(Playlist p) {
        
    }
    
    public void add(String path) {
        final File f = new File("VIDEOS/"+path);
        final Media media = new Media(f.toURI().toString());
        final MediaPlayer player = new MediaPlayer(media);
        player.setOnEndOfMedia(new Runnable() {
        @Override public void run() {
            players_wait.get(0).play();
            player.stop();
            mv.setMediaPlayer(players_wait.get(0));
                try{
                Thread.sleep(30000);
                }catch(InterruptedException e){}
            position++;
            mv.setMediaPlayer(players.get(position));
            players.get(position).play();
        }});
        players.add(player);
    }
    
    public void play(int position) {
        this.position = position;
        mv.setMediaPlayer(players.get(position));
        
        final DoubleProperty width = mv.fitWidthProperty();
        final DoubleProperty height = mv.fitHeightProperty();
        
        width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));

        mv.setPreserveRatio(true);

        StackPane root = new StackPane();
        root.getChildren().add(mv);

        final Scene scene = new Scene(root, 960, 540);
        scene.setFill(Color.BLACK);

        stage.setScene(scene);
        stage.setTitle("Full Screen Video Player");
        //stage.setFullScreen(true);
        stage.show();

        players.get(position).play();
    }
    
    public void setPlayerWait(String path) {
        final File f = new File("VIDEOS/"+path);
        final Media media = new Media(f.toURI().toString());
        final MediaPlayer player = new MediaPlayer(media);
        players_wait.add(player);
    }
    
}
