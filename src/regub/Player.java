/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

/**
 *
 * @author vinc
 */
public class Player {
    List<MediaPlayer> players = new ArrayList<>();
    List<MediaPlayer> players_wait = new ArrayList<>();
    int position = 0;
    MediaView mv = new MediaView();
    
    public Player(Playlist p) {
        
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
    
    
    
    public void setPlayerWait(String path) {
        final File f = new File("VIDEOS/"+path);
        final Media media = new Media(f.toURI().toString());
        final MediaPlayer player = new MediaPlayer(media);
        players_wait.add(player);
    }
    
}