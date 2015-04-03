/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub_local;

import java.util.ArrayList;

/**
 *
 * @author P A U L  A L B A R E L L O
 */
public class Playlist {
	
    private float heure_debut_diffusion;
    private int duree_diffusion;
    private int duree_entre_videos;
    private int[] ordre_diffusion;

    public Playlist(float heure_debut_diffusion, float heure_fin_diffusion, ArrayList<Contrat> liste_contrats) {
        setHeureDebutDiffusion(heure_debut_diffusion);
        setDureeDiffusion((int) ((heure_fin_diffusion - heure_debut_diffusion) * 3600));
        creerPlanification(liste_contrats);
    }

    private void creerPlanification(ArrayList<Contrat> liste_contrats) {
        int temps_total = this.getDureeDiffusion();
        int somme_frequence = 0;
        for (Contrat contrat : liste_contrats) {
            somme_frequence += contrat.frequence;
        }
        this.ordre_diffusion = new int[somme_frequence];
        int temps_entre_videos = (int) (temps_total / (somme_frequence + 1));
        setDureeEntreVideos(temps_entre_videos);
        ArrayList<Integer> tableau_temps_diffusions = new ArrayList<>();
        for (int i=1; i<=somme_frequence; i++) {
            tableau_temps_diffusions.add(i * temps_entre_videos);
        }
        int espacement_contrat;
        for (Contrat contrat : liste_contrats) {
            espacement_contrat = temps_total / (contrat.frequence+1);
            int temps_ideal;
            int temps_retenu;
            for (int i=1; i<=contrat.frequence; i++) {
                temps_ideal = i*espacement_contrat;
                temps_retenu = rechercheDichotomique(tableau_temps_diffusions, temps_ideal);
                this.ordre_diffusion[temps_retenu] = contrat.idVideo;
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
                    return debut+1;
            }
        }
        return debut;
    }

    private void setHeureDebutDiffusion(float heure_debut_diffusion) {
        this.heure_debut_diffusion = heure_debut_diffusion;
    }

    public float getHeureDebutDiffusion() {
        return this.heure_debut_diffusion;
    }

    private void setDureeDiffusion(int duree_diffusion) {
        this.duree_diffusion = duree_diffusion;       
    }

    public int getDureeDiffusion() {
        return this.duree_diffusion;
    }

    private void setDureeEntreVideos(int duree_entre_videos) {
        this.duree_entre_videos = duree_entre_videos;       
    }

    public int getDureeEntreVideos() {
        return this.duree_entre_videos;
    }
    
    public int[] getOrdreDiffusion() {
        return this.ordre_diffusion;
    }
}
