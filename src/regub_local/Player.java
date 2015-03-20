/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub_local;

import java.io.File;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author vinc
 */
public class Player {
    private Stage stage;
    
    public Player(Stage stage) {
        this.stage = stage;
    }
    
    public void play(String path) {
        final File f = new File(path);

        final Media m = new Media(f.toURI().toString());
        final MediaPlayer mp = new MediaPlayer(m);
        
        final MediaView mv = new MediaView(mp);

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
        stage.setFullScreen(true);
        stage.show();

        mp.play();
    }
}
