/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub;

import java.util.Calendar;

/**
 *
 * @author PaulA
 */
public class Diffusion {
    
    private Contrat contrat;
    private Calendar heure_diffusion;
    
    public Diffusion(Contrat contrat, Calendar heure_diffusion) {
        this.contrat = contrat;
        this.heure_diffusion = heure_diffusion;
    }
    
    public void setContrat(Contrat contrat) {
        this.contrat = contrat;
    }
    
    public void setHeureDiffusion(Calendar heure_diffusion) {
        this.heure_diffusion = heure_diffusion;
    }
    
    public Contrat getContrat() {
        return this.contrat;
    }
    
    public Calendar getHeureDiffusion() {
        return this.heure_diffusion;
    } 
}
