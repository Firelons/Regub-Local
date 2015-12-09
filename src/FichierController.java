
import java.io.*;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FichierController {
    
    private static FichierController INSTANCE; 

    private static final String DOSSIER_LOGS_DIFFUSIONS = "logs/diffusions";
    private static final String DOSSIER_LOGS_SYSTEME = "logs/systeme";
    private static final String FICHIER_CONTRATS = "contrats";
    private static final String FICHIER_CONTRATS_REPORTES = "contrats_reportes";
    
    public static FichierController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FichierController();
        }
        return INSTANCE;
    }
    
    public static void loguer_diffusion(Diffusion dif) throws IOException {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
         
        
        sb.append(DOSSIER_LOGS_DIFFUSIONS).append("/")
                .append(String.format("%02d", dif.getHeureDiffusion().get(Calendar.YEAR))).append("-")
                .append(String.format("%02d", dif.getHeureDiffusion().get(Calendar.MONTH)+1)).append("-")
                .append(String.format("%02d", dif.getHeureDiffusion().get(Calendar.DAY_OF_MONTH)))
                .append(".txt");
        try (BufferedWriter output = new BufferedWriter(new FileWriter(sb.toString(), true))) {
            sb = new StringBuilder();
            
            sb1.append(String.format("%02d", dif.getHeureDiffusion().get(Calendar.YEAR))).append("-")
                .append(String.format("%02d", dif.getHeureDiffusion().get(Calendar.MONTH)+1)).append("-")
                .append(String.format("%02d", dif.getHeureDiffusion().get(Calendar.DAY_OF_MONTH)));
                
            
            sb.append(String.format("%02d", dif.getHeureDiffusion().get(Calendar.HOUR_OF_DAY))).append(":")
                    .append(String.format("%02d", dif.getHeureDiffusion().get(Calendar.MINUTE))).append(":")
                    .append(String.format("%02d", dif.getHeureDiffusion().get(Calendar.SECOND)));
                 
            
            sb2.append(sb1).append(" ").append(sb).append(" ").append(dif.getContrat().getIdVideo());
           
            
            output.append(sb2.toString());
            output.newLine();
        }    
    } 
    
    public static void loguer_systeme(String message) {
        StringBuilder sb = new StringBuilder();
        Calendar cal = Calendar.getInstance();
        sb.append(DOSSIER_LOGS_SYSTEME).append("/")
                .append(String.format("%02d", cal.get(Calendar.YEAR))).append("-")
                .append(String.format("%02d", cal.get(Calendar.MONTH)+1)).append("-")
                .append(String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)))
                .append(".txt");
        try (BufferedWriter output = new BufferedWriter(new FileWriter(sb.toString(), true))) {
            sb = new StringBuilder();
            sb.append("[").append(String.format("%02d", cal.get(Calendar.HOUR_OF_DAY))).append(":")
                    .append(String.format("%02d", cal.get(Calendar.MINUTE))).append(":")
                    .append(String.format("%02d", cal.get(Calendar.SECOND))).append("] ").append(message);
            output.append(sb.toString());
            output.newLine();
        } catch (IOException ex) {
            Logger.getLogger(FichierController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    public void sauverContratsADiffuser(ArrayList<Contrat> contrats_a_diffuser) throws RegubException {
        try {
            FileOutputStream fos;
            fos = new FileOutputStream("fichiers/"+FICHIER_CONTRATS);
            try (ObjectOutputStream out = new ObjectOutputStream(fos)) {
                out.writeObject(contrats_a_diffuser);
                out.close();
            }
            fos.close();
        } catch (FileNotFoundException ex) {
            throw new RegubException("Le fichier des contrats est introuvable.");
        } catch (IOException ex) {
            throw new RegubException("Erreur lors de l'écriture du fichier des contrats.");
        }
    }
    
    public ArrayList<Contrat> chargerContratsADiffuser() throws RegubException {
        ArrayList<Contrat> contrats_a_diffuser = new ArrayList();
        FileInputStream fis;
        try {
            fis = new FileInputStream("fichiers/"+FICHIER_CONTRATS);
            try (ObjectInputStream in = new ObjectInputStream(fis)) {
                contrats_a_diffuser = (ArrayList<Contrat>) in.readObject();
                in.close();
            }
            fis.close();
        } catch (FileNotFoundException ex) {
            throw new RegubException("Le fichier des contrats est introuvable.");
        } catch (IOException | ClassNotFoundException ex) {
            throw new RegubException("Erreur lors de la lecture du fichier des contrats.");
        }
        return contrats_a_diffuser;
    }
    
    public void sauverContratsReportes(ArrayList<Contrat> contrats_reportes) throws RegubException {
        try {
            FileOutputStream fos;
            fos = new FileOutputStream("fichiers/"+FICHIER_CONTRATS_REPORTES);
            try (ObjectOutputStream out = new ObjectOutputStream(fos)) {
                out.writeObject(contrats_reportes);
                out.close();
            }
            fos.close();
        } catch (FileNotFoundException ex) {
            throw new RegubException("Le fichier des contrats reportés est introuvable.");
        } catch (IOException ex) {
            throw new RegubException("Erreur lors de la lecture du fichier des contrats reportés.");
        }
    }
    
    public ArrayList<Contrat> chargerContratsReportes() {
        ArrayList<Contrat> contrats_reportes = new ArrayList();
        FileInputStream fis;
        try {
            fis = new FileInputStream("fichiers/"+FICHIER_CONTRATS_REPORTES);
            try (ObjectInputStream in = new ObjectInputStream(fis)) {
                contrats_reportes = (ArrayList<Contrat>) in.readObject();
                in.close();
            }
            fis.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Contrat local non existant...");
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contrats_reportes;
    }
    
    
    
    private String IP;
    private String PATH;

    FichierController() {
        try{
            Configuration conf = Configuration.getInstance();
            IP = conf.getProp("web_url");
            PATH = conf.getProp("video_path");
        }
        catch(RegubException ex)
        {
            Logger.getLogger(FichierController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void download(String name) {
        URL website;
        try {
            website = new URL(IP + name);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(PATH + name);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (MalformedURLException ex) {
            Logger.getLogger(FichierController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FichierController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(ArrayList<Contrat> contrat) {
        Iterator<Contrat> it = contrat.iterator();
        while (it.hasNext()) {
            Contrat cm_local = it.next();
            String path = PATH + cm_local.getIdVideo() + ".mp4";
            File file = new File(path);
            file.delete();
            System.out.println("!SUPPRIMER:" + path);
        }
    }
}
