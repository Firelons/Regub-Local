/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub;

import java.util.ArrayList;

/**
 *
 * @author PaulA
 */
public class MathUtil {
    
    public static int rechercheDichotomique(ArrayList<Integer> tab, int val) {
        boolean trouve;
        int debut, fin, milieu;

        trouve = false;
        debut = 0;
        fin = tab.size();

        while (!trouve && ((fin - debut) > 1)) {
            milieu = (debut+fin)/2;
            trouve = (tab.get(milieu) == val);
            if (tab.get(milieu) > val) fin = milieu;
            else debut = milieu;
        }

        if (debut<tab.size()-1) {
            if ((tab.get(debut+1) - val) < val - tab.get(debut)) {
                    return tab.get(debut+1);
            }
        }
        return tab.get(debut);
    }
}
