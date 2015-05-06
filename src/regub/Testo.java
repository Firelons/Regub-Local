/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author Paul
 */
public class Testo {
    
    public static void main(String[] args) {
        
        ArrayList<Contrat> liste_contrats = new ArrayList<>();
        
        Calendar debut0 = Calendar.getInstance(); Calendar fin0 = Calendar.getInstance();
        debut0.set(Calendar.YEAR, 2015); fin0.set(Calendar.YEAR, 2015);
        debut0.set(Calendar.MONTH, 3); fin0.set(Calendar.MONTH, 9);
        debut0.set(Calendar.DAY_OF_MONTH, 1); fin0.set(Calendar.DAY_OF_MONTH, 1);
        Contrat c0 = new Contrat(0, "ha! gaaaaay!", 4, 2, debut0, fin0);
        
        Calendar debut1 = Calendar.getInstance(); Calendar fin1 = Calendar.getInstance();
        debut1.set(Calendar.YEAR, 2015); fin1.set(Calendar.YEAR, 2015);
        debut1.set(Calendar.MONTH, 3); fin1.set(Calendar.MONTH, 9);
        debut1.set(Calendar.DAY_OF_MONTH, 1); fin1.set(Calendar.DAY_OF_MONTH, 1);
        Contrat c1 = new Contrat(1, "cookies", 5, 8, debut1, fin1);
        
        Calendar debut2 = Calendar.getInstance(); Calendar fin2 = Calendar.getInstance();
        debut2.set(Calendar.YEAR, 2015); fin2.set(Calendar.YEAR, 2015);
        debut2.set(Calendar.MONTH, 3); fin2.set(Calendar.MONTH, 9);
        debut2.set(Calendar.DAY_OF_MONTH, 1); fin2.set(Calendar.DAY_OF_MONTH, 1);
        Contrat c2 = new Contrat(2, "fuck you asshole!", 4, 5, debut2, fin2);
        
        Calendar debut3 = Calendar.getInstance(); Calendar fin3 = Calendar.getInstance();
        debut3.set(Calendar.YEAR, 2015); fin3.set(Calendar.YEAR, 2015);
        debut3.set(Calendar.MONTH, 3); fin3.set(Calendar.MONTH, 9);
        debut3.set(Calendar.DAY_OF_MONTH, 1); fin3.set(Calendar.DAY_OF_MONTH, 1);
        Contrat c3 = new Contrat(3, "surprise motherfucker!", 6, 4, debut3, fin3);
        
        Calendar debut4 = Calendar.getInstance(); Calendar fin4 = Calendar.getInstance();
        debut4.set(Calendar.YEAR, 2015); fin4.set(Calendar.YEAR, 2015);
        debut4.set(Calendar.MONTH, 3); fin4.set(Calendar.MONTH, 9);
        debut4.set(Calendar.DAY_OF_MONTH, 1); fin4.set(Calendar.DAY_OF_MONTH, 1);
        Contrat c4 = new Contrat(4, "what a story mark", 4, 3, debut4, fin4);
        
        Calendar debut5 = Calendar.getInstance(); Calendar fin5 = Calendar.getInstance();
        debut5.set(Calendar.YEAR, 2015); fin5.set(Calendar.YEAR, 2015);
        debut5.set(Calendar.MONTH, 3); fin5.set(Calendar.MONTH, 9);
        debut5.set(Calendar.DAY_OF_MONTH, 1); fin5.set(Calendar.DAY_OF_MONTH, 1);
        Contrat c5 = new Contrat(5, "bazinga", 10, 2, debut5, fin5);
        
        liste_contrats.add(c0);
        liste_contrats.add(c1);
        liste_contrats.add(c2);
        liste_contrats.add(c3);
        liste_contrats.add(c4);
        liste_contrats.add(c5);
        
        try {
            FileOutputStream fos;
            fos = new FileOutputStream("contrats");
            try (ObjectOutputStream out = new ObjectOutputStream(fos)) {
                out.writeObject(liste_contrats);
                out.close();
            }
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
