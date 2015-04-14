/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub_local;

import java.util.ArrayList;
import java.util.Calendar;
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
        
        
        System.out.println();  System.out.println("|||||||| DEBUGGER ||||||||||");
        
        /*Property properties = new Property();
        
        FileController fc_root = new FileController("");
        ArrayList<Contrat> contrat_local = fc_root.serLoad("contrats");

        ContratController cc = ContratController.getInstance();
        ArrayList<Contrat> contrat_remote = cc.getContrats(properties.getProp("rayon"));
        
        FileController fc_video = new FileController("VIDEOS/", "http://172.16.0.50/");
        
        boolean existe; int cpt = 0;
        Iterator<Contrat> it1 = contrat_remote.iterator();
        while (it1.hasNext()) {
            Contrat cm_remote = it1.next();
            existe = false;
            Iterator<Contrat> it2 = contrat_local.iterator();
            while (it2.hasNext() && !(existe)) {
                Contrat cm_local = it2.next();
                if (cm_remote.idVideo == cm_local.idVideo) {
                    existe = true;
                    contrat_local.remove(cm_local);
                }
            }
            if (!(existe)) {
                fc_video.download(Integer.toString(cm_remote.idVideo)+".mp4");
                System.out.println("#TELECHARGEMENT:" + cm_remote.idVideo + ".MP4");
            }
            cpt++;
        }
        
        if (cpt > 0) {
            fc_video.delete(contrat_local);
            fc_root.serSave("contrats", contrat_remote);
        }*/
        
        
        //Playlist p = new Playlist(8, 18, contrat_local);
        Player video = new Player(stage);
        video.setPlayerWait("videowait.mp4");
        video.add("1.mp4");
        video.add("4.mp4");
        video.play(0);
        System.out.println("|||||||| END ||||||||||");
    }

    public static void main(String[] args) {
        launch(args);
    }

}
