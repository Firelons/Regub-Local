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
        //Serialize serialize = new Serialize("contrats");
        //serialize.read();
        //ContratController cc = new ContratController("Poissonerie");
        //serialize.write(cc.getList());
        //serialize.compare(cc.getList());
        
        Player video = new Player(stage);
        video.play("test.mp4");
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
