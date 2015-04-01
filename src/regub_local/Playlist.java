/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub_local;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author vinc
 */
public class Playlist {
    public static void main(String[] args) {
        String heure = "8.5-18.5";
        String horaire[] = heure.split("-");
        float ouverture = Float.parseFloat(horaire[0]);
        float fermeture = Float.parseFloat(horaire[1]);
        int temps_total = (int) (fermeture - ouverture) * 3600;
        
        System.out.println("Ouverture:" + ouverture);
        System.out.println("Fermeture:" + fermeture);
        System.out.println("Temps total:" + temps_total);
        
        ArrayList<ContratModel> contrat_local = new ArrayList();
        ContratModel cm1 = new ContratModel();
        cm1.idVideo = 1; cm1.frequence = 10;
        ContratModel cm2 = new ContratModel();
        cm2.idVideo = 1; cm2.frequence = 7;
        ContratModel cm3 = new ContratModel();
        cm3.idVideo = 1; cm3.frequence = 4;
        ContratModel cm4 = new ContratModel();
        cm4.idVideo = 1; cm4.frequence = 2;
        ContratModel cm5 = new ContratModel();
        cm5.idVideo = 1; cm5.frequence = 1;
        
        contrat_local.add(cm1);
        contrat_local.add(cm2);
        contrat_local.add(cm3);
        contrat_local.add(cm4);
        contrat_local.add(cm5);
        
        int totalFreq = 0;
        for (ContratModel c : contrat_local) {
            totalFreq+=c.frequence;
        }
        int espacementDif = (int) (temps_total / (totalFreq + 1));
        System.out.println("Espacement diff:"+espacementDif);
        
        HashMap<Integer,Boolean> tab1 = new HashMap<>();
        HashMap<Integer,Integer> tab2 = new HashMap<>();
        for (int i = 1; i <= totalFreq; i++) {
            tab1.put(i * espacementDif, Boolean.FALSE);
            tab2.put(i * espacementDif, 0);
        }
        
        int espacementVideo;
        for (ContratModel c : contrat_local) {
            espacementVideo = temps_total/(c.frequence+1);
            int heureIdeale;
            for (int i=1; i<=c.frequence; i++) {
                heureIdeale = i*espacementVideo;
                
            }
        }
    }
}
