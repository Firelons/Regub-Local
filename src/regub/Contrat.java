
package regub;

import java.util.Calendar;

public class Contrat implements java.io.Serializable {
    private int idVideo;
    private String titre;
    private int frequence;
    private int duree;
    private Calendar dateDebut;
    private Calendar dateFin;   
    
    public Contrat(int idVideo, String titre, int frequence, int duree, Calendar dateDebut, Calendar dateFin) {
        this.idVideo = idVideo;
        this.titre = titre;
        this.frequence = frequence;
        this.duree = duree;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }
    
    public void setIdVideo(int idVideo) {
        this.idVideo = idVideo;
    }
    
    public int getIdVideo() {
        return this.idVideo;
    }
    
    public void setTitre(String titre) {
        this.titre = titre;
    }
    
    public String getTitre() {
        return titre;
    }
    
    public void setFrequence(int frequence) {
        this.frequence = frequence;
    }
    
    public int getFrequence() {
        return this.frequence;
    }
    
    public void setDuree(int duree) {
        this.duree = duree;
    }
    
    public int getDuree() {
        return this.duree;
    }
    
    public void setDateDebut(Calendar dateDebut) {
        this.dateDebut = dateDebut;
    }
    
    public Calendar getDateDebut() {
        return this.dateDebut;
    }
    
    public void setDateFin(Calendar dateFin) {
        this.dateFin = dateFin;
    }
    
    public Calendar getDateFin() {
        return this.dateFin;
    }
}