
package regub;

import java.util.ArrayList;

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
        Contrat c0 = new Contrat();
        Contrat c1 = new Contrat();
        Contrat c2 = new Contrat();
        Contrat c3 = new Contrat();
        Contrat c4 = new Contrat();
        Contrat c5 = new Contrat();
        
        c5.setFrequence(10); c5.setIdVideo(5); c5.setTitre("bazinga");
        c1.setFrequence(4); c1.setIdVideo(1); c1.setTitre("cookies");
        c4.setFrequence(3); c4.setIdVideo(4); c4.setTitre("what a story mark");
        c2.setFrequence(2); c2.setIdVideo(2); c2.setTitre("fuck you asshole!");
        c0.setFrequence(2); c0.setIdVideo(0); c0.setTitre("ah! gayyyyyy!");
        c3.setFrequence(1); c3.setIdVideo(3); c3.setTitre("surprise mothafucka");
        
        ArrayList<Contrat> tab = new ArrayList<>();
        tab.add(c5);
        tab.add(c1);tab.add(c4); tab.add(c2); tab.add(c0); tab.add(c3);  
        
        Playlist p = new Playlist(8, (float) 8.2, tab);
        
        return p;
    }
}
