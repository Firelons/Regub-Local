
package regub;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paul
 */
public class PlaylistController {
    
    private static PlaylistController INSTANCE;

    public static PlaylistController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlaylistController();
        }
        return INSTANCE;
    }
    
    public Playlist getPlaylist() {
        
        
       
        
        
        /*Contrat c0 = new Contrat();
        Contrat c1 = new Contrat();
        Contrat c2 = new Contrat();
        Contrat c3 = new Contrat();
        Contrat c4 = new Contrat();
        Contrat c5 = new Contrat();
        
        c5.setFrequence(10); c5.setIdVideo(5); c5.setTitre("bazinga"); c5.setDuree(2);
        c1.setFrequence(4); c1.setIdVideo(1); c1.setTitre("cookies"); c1.setDuree(8);
        c4.setFrequence(3); c4.setIdVideo(4); c4.setTitre("what a story mark"); c4.setDuree(3);
        c2.setFrequence(2); c2.setIdVideo(2); c2.setTitre("fuck you asshole!"); c2.setDuree(5);
        c0.setFrequence(2); c0.setIdVideo(0); c0.setTitre("ah! gayyyyyy!"); c0.setDuree(2);
        c3.setFrequence(1); c3.setIdVideo(3); c3.setTitre("surprise mothafucka"); c3.setDuree(4);
        
        ArrayList<Contrat> tab = new ArrayList<>();
        tab.add(c5);
        tab.add(c1);tab.add(c4); tab.add(c2); tab.add(c0); tab.add(c3);  
        
        Calendar[] horaires = null;
        try {
            horaires = Configuration.getInstance().getHours(Calendar.SUNDAY);
        } catch (IOException ex) {
            Logger.getLogger(PlaylistController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Playlist p = new Playlist(horaires[0], horaires[1], tab);
        
        
        
        try {
            FileOutputStream fos;
            fos = new FileOutputStream("contrats"); //name='contrats'
            try (ObjectOutputStream out = new ObjectOutputStream(fos)) {
                out.writeObject(tab);
                out.close();
            }
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
        */
        
        
        
        
        
        
        
        
        return null;
    }
}
