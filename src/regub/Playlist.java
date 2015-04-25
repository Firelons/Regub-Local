
package regub;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 *
 * @author P A U L  A L B A R E L L O
 */
public class Playlist {
	
    private ArrayList<Contrat> liste_contrats;
    private Calendar heure_debut_diffusion;
    private Calendar heure_fin_diffusion;
    private int duree_diffusion;
    private int duree_entre_videos;
    private int[] ordre_diffusion;
    
    public Playlist(Calendar heure_debut_diffusion, Calendar heure_fin_diffusion, ArrayList<Contrat> liste_contrats) {
        this.liste_contrats = liste_contrats;
        setHeureDebutDiffusion(heure_debut_diffusion);
        setHeureFinDiffusion(heure_fin_diffusion);
        setDureeDiffusion((int) (heure_fin_diffusion.getTimeInMillis() - heure_debut_diffusion.getTimeInMillis()) / 1000);
        
        creerPlanification(liste_contrats);
    }

    private void creerPlanification(ArrayList<Contrat> liste_contrats) {
        int temps_total = this.getDureeDiffusion();
        int somme_frequence = 0;
        for (Contrat contrat : liste_contrats) {
            somme_frequence += contrat.getFrequence();
        }
        this.ordre_diffusion = new int[somme_frequence];
        int temps_entre_videos = (int) (temps_total / (somme_frequence + 1));
        setDureeEntreVideos(temps_entre_videos);
        System.out.println("durée diffusion : " + this.getDureeDiffusion());
        System.out.println("durée entre vidéo : " + this.getDureeEntreVideos());
        HashMap<Integer, Integer> tableau_temps_diffusions_index = new HashMap<>();
        ArrayList<Integer> tableau_temps_diffusions = new ArrayList<>();
        for (int i=1; i<=somme_frequence; i++) {
            tableau_temps_diffusions_index.put(i * temps_entre_videos, i-1);
            tableau_temps_diffusions.add(i * temps_entre_videos);
        }
        int espacement_contrat;
        for (Contrat contrat : liste_contrats) {
            espacement_contrat = temps_total / (contrat.getFrequence()+1);
            int temps_ideal;
            int temps_retenu;
            for (int i=1; i<=contrat.getFrequence(); i++) {
                temps_ideal = i*espacement_contrat;
                temps_retenu = rechercheDichotomique(tableau_temps_diffusions, temps_ideal);
                this.ordre_diffusion[tableau_temps_diffusions_index.get(temps_retenu)] = contrat.getIdVideo();
                tableau_temps_diffusions.remove(tableau_temps_diffusions.indexOf(temps_retenu));
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

    private void setHeureDebutDiffusion(Calendar heure_debut_diffusion) {
        this.heure_debut_diffusion = heure_debut_diffusion;
    }

    public Calendar getHeureDebutDiffusion() {
        return this.heure_debut_diffusion;
    }
    
    private void setHeureFinDiffusion(Calendar heure_fin_diffusion) {
        this.heure_fin_diffusion = heure_fin_diffusion;
    }

    public Calendar getHeureFinDiffusion() {
        return this.heure_fin_diffusion;
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
    
    public void setListeContrats(ArrayList<Contrat> liste_contrats) {
        this.liste_contrats = liste_contrats;
    }
    
    public ArrayList<Contrat> getListeContrats() {
        return this.liste_contrats;
    }
    
    public Contrat getContrat(int idVideo) {
        for (Contrat c : this.liste_contrats) {
            if (c.getIdVideo() == idVideo) {
                return c;
            }
        }
        return null;
    }
}