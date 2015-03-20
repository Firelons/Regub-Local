/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub_local;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author vinc
 */
public class Regub_local extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        //BDD : 172.16.0.50 - user root - mdp jtankhull
        
        Player video = new Player(stage);
        video.play("test.mp4");
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
