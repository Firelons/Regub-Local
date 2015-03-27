/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub_local;

import java.util.ArrayList;
import java.util.Iterator;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author vinc
 */
public class Regub_local extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FileController fc = new FileController("");
        ArrayList<ContratModel> contrat_local = fc.serLoad("contrats");

        ContratController cc = ContratController.getInstance();
        ArrayList<ContratModel> contrat_remote = cc.getContrat("Charcuterie");
        
        System.out.println("-Contrat local:");
        System.out.println(contrat_local.toString());
        System.out.println("-Contrat remote:");
        System.out.println(contrat_remote.toString());
        
        fc.setPath("VIDEOS/");
        boolean existe;
        Iterator<ContratModel> it1 = contrat_remote.iterator();
        while (it1.hasNext()) {
            ContratModel cm_remote = it1.next();
            existe = false;
            Iterator<ContratModel> it2 = contrat_local.iterator();
            while (it2.hasNext()) {
                ContratModel cm_local = it2.next();
                if (cm_remote.idVideo == cm_local.idVideo) {
                    existe = true;
                    contrat_local.remove(cm_local);
                }
            }
            if (!(existe)) {
                fc.download(Integer.toString(cm_remote.idVideo)+".mp4");
                System.out.println("#TELECHARGEMENT:" + cm_remote.idVideo + ".MP4");
            }
        }
        
        fc.delete(contrat_local);
fc.setPath("");
        fc.serSave("contrats", contrat_remote);

        Player video = new Player(stage);
        //video.play("test.mp4");
    }

    public static void main(String[] args) {
        launch(args);
    }

}
