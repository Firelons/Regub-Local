
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
    private Calendar debut_diffusion;
    private Calendar fin_diffusion;
    private int[] ordre_diffusion;
    private ArrayList<Diffusion> liste_diffusion;
    
    public Playlist(Calendar debut_diffusion, Calendar fin_diffusion, ArrayList<Contrat> liste_contrats) throws RegubException {
        if (debut_diffusion.after(fin_diffusion)) throw new RegubException("Erreur : la date de début de diffusion est supérieur à la date de fin de diffusion");
        this.liste_contrats = liste_contrats;
        this.debut_diffusion = debut_diffusion;
        this.fin_diffusion = fin_diffusion;
        this.liste_diffusion = new ArrayList<>();
        
        creerPlanification(liste_contrats);
    }
    
    private void creerPlanification(ArrayList<Contrat> liste_contrats) {
        int somme_frequences = this.getSommeFrequences();
        this.ordre_diffusion = new int[somme_frequences];
        int intervalle = (int)(this.getDureeDiffusion() / (somme_frequences + 1));
        
        HashMap<Integer, Integer> tableau_temps_diffusions_index = new HashMap<>();
        ArrayList<Integer> tableau_temps_diffusions = new ArrayList<>();
        for (int i=1; i<=somme_frequences; i++) {
            tableau_temps_diffusions_index.put(i * intervalle, i-1);
            tableau_temps_diffusions.add(i * intervalle);
        }
        int espacement_contrat;
        for (Contrat contrat : liste_contrats) {
            espacement_contrat = this.getDureeDiffusion() / (contrat.getFrequence()+1);
            int temps_ideal;
            int temps_retenu;
            for (int i=1; i<=contrat.getFrequence(); i++) {
                temps_ideal = i*espacement_contrat;
                temps_retenu = rechercheDichotomique(tableau_temps_diffusions, temps_ideal);
                this.ordre_diffusion[tableau_temps_diffusions_index.get(temps_retenu)] = contrat.getIdVideo();
                tableau_temps_diffusions.remove(tableau_temps_diffusions.indexOf(temps_retenu));
            }
        }
        
        int debut = 0;
        int duree_pause = this.getDureePause();
        
        for (Integer i : ordre_diffusion) {
            Contrat cont = null;
            for (Contrat c : liste_contrats) {
                if (c.getIdVideo() == i) {
                    cont = c;
                }
            }
           
            debut = debut + duree_pause;
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, this.debut_diffusion.get(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, this.debut_diffusion.get(Calendar.MINUTE));
            cal.set(Calendar.SECOND, this.debut_diffusion.get(Calendar.SECOND));
            cal.add(Calendar.SECOND, debut);
            liste_diffusion.add(new Diffusion(cont, cal));
            debut = debut + cont.getDuree();
        }
        
        System.out.println("durée entre videos : "+duree_pause);
        for (Diffusion d : this.liste_diffusion) {
            System.out.println(d.getHeureDiffusion().get(Calendar.HOUR_OF_DAY)+":"+
                    d.getHeureDiffusion().get(Calendar.MINUTE)+":"+
                    d.getHeureDiffusion().get(Calendar.SECOND)+ " Contrat n°" + d.getContrat().getIdVideo()+
                    " : " + d.getContrat().getTitre());
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

    public void setDebutDiffusion(Calendar debut_diffusion) {
        this.debut_diffusion = debut_diffusion;
    }

    public Calendar getDebutDiffusion() {
        return this.debut_diffusion;
    }
    
    public void setFinDiffusion(Calendar fin_diffusion) {
        this.fin_diffusion = fin_diffusion;
    }

    public Calendar getFinDiffusion() {
        return this.fin_diffusion;
    }
    
    public void setListeDiffusion(ArrayList<Diffusion> liste_diffusion) {
        this.liste_diffusion = liste_diffusion;
    }
    
    public ArrayList<Diffusion> getListeDiffusion() {
        return this.liste_diffusion;
    }

    public int getDureeDiffusion() {
        return (int)((this.fin_diffusion.getTimeInMillis() - this.debut_diffusion.getTimeInMillis()) / 1000);
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
    
    private int getSommeFrequences() {
        int somme_frequences = 0;
        somme_frequences = liste_contrats.stream().map((contrat) -> contrat.getFrequence()).reduce(somme_frequences, Integer::sum);
        return somme_frequences;
    }
    
    private int getSommeDurees() {
        int somme_durees = 0;
        for (Contrat c : this.liste_contrats) {
            somme_durees = somme_durees + (c.getFrequence() * c.getDuree());
        }
        return somme_durees;
    }
    
    private int getDureePause() {
        return (int)((this.getDureeDiffusion() - this.getSommeDurees())/(this.getSommeFrequences() + 1));
    }
}