
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Landry Emmanuel
 */
public class video {
    private int idVideo;
    private String titre;
    private int frequence;
    private int duree;
    private Date dateDebut;
    private Date dateFin;
    private Date dateReception;
    private Date dateValidation;
    private int tarif;
    private int statut;
    private int idMagasin;
    private int idClient;

    public video(int idVideo, String titre, int frequence, int duree, Date dateDebut, Date dateFin, int idCommercial, int idClient) {
        this.idVideo = idVideo;
        this.titre = titre;
        this.frequence = frequence;
        this.duree = duree;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.idMagasin = idCommercial;
        this.idClient = idClient;
    }

    public video(int idVideo, String titre, int frequence, int duree, Date dateDebut, Date dateFin, Date dateRecception, Date dateValidation, int tarif, int statut, int idCommercial, int idClient) {
        this.idVideo = idVideo;
        this.titre = titre;
        this.frequence = frequence;
        this.duree = duree;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.dateReception = dateRecception;
        this.dateValidation = dateValidation;
        this.tarif = tarif;
        this.statut = statut;
        this.idMagasin = idCommercial;
        this.idClient = idClient;
    }

    public int getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(int idVideo) {
        this.idVideo = idVideo;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public int getFrequence() {
        return frequence;
    }

    public void setFrequence(int frequence) {
        this.frequence = frequence;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Date getDateReception() {
        return dateReception;
    }

    public void setDateReception(Date dateReception) {
        this.dateReception = dateReception;
    }

    public Date getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(Date dateValidation) {
        this.dateValidation = dateValidation;
    }

    public int getTarif() {
        return tarif;
    }

    public void setTarif(int tarif) {
        this.tarif = tarif;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public int getIdMagasin() {
        return idMagasin;
    }

    public void setIdMagasin(int idMagasin) {
        this.idMagasin = idMagasin;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }
    

    
    
}
