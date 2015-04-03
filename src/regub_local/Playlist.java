/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub_local;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author vinc
 */
public class Playlist {
	
	private int jour;
	private float heureOuverture;
	private float heureFermeture;
	private TreeMap<Integer, Integer> planification;
	
	public Playlist(ArrayList<ContratModel> liste_contrats) {
            creerPlanification(liste_contrats);
	}
	
	private void creerPlanification(ArrayList<ContratModel> liste_contrats) {
            int temps_total = this.getTempsTotal();
            int somme_frequence = 0;
            for (ContratModel contrat : liste_contrats) {
                somme_frequence += contrat.frequence;
            }
            int temps_entre_diffusions = (int) (temps_total / (somme_frequence + 1));
            ArrayList<Integer> tableau_temps_diffusions = new ArrayList<>();
            for (int i=1; i<=somme_frequence; i++) {
                tableau_temps_diffusions.add(i * temps_entre_diffusions);
                this.planification.put(i * temps_entre_diffusions, -1);
            }
            int espacement_contrat;
            for (ContratModel contrat : liste_contrats) {
                espacement_contrat = temps_total / (contrat.frequence+1);
                int temps_ideal;
                int temps_retenu;
                for (int i=1; i<=contrat.frequence; i++) {
                    temps_ideal = i*espacement_contrat;
                    temps_retenu = rechercheDichotomique(tableau_temps_diffusions, temps_ideal);
                    this.planification.put(temps_retenu, contrat.idVideo);
                    tableau_temps_diffusions.remove(temps_retenu);
                }
            }
	}
        
        int rechercheDichotomique(ArrayList<Integer> tab, int val) {
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
	
	public void setHeureOuverture(float heureOuverture) {
		this.heureOuverture = heureOuverture;
	}
	
	public float getHeureOuverture() {
		return this.heureOuverture;
	}
	
	public void setHeureFermeture(float heureFermeture) {
		this.heureFermeture = heureFermeture;
	}
	
	public float getHeureFermeture() {
		return this.heureFermeture;
	}
	
	public int getTempsTotal() {
		return (int) (this.heureFermeture - this.heureOuverture) * 3600;
	}
	
	public void setJour(int jour) {
		this.jour = jour;
	}
	
	public int getJour() {
		return this.jour;
	}	
}
