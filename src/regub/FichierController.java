package regub;

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

    private static final String DOSSIER_LOGS_DIFFUSION = "logs_diffusion";
    
    public static FichierController getInstance() throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new FichierController();
        }
        return INSTANCE;
    }
    
    public static void loguer_diffusion(Diffusion dif) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(DOSSIER_LOGS_DIFFUSION).append("/")
                .append(String.format("%02d", dif.getHeureDiffusion().get(Calendar.DAY_OF_MONTH)))
                .append(String.format("%02d", dif.getHeureDiffusion().get(Calendar.MONTH)))
                .append(String.format("%02d", dif.getHeureDiffusion().get(Calendar.YEAR)))
                .append(".txt");
        try (Writer output = new BufferedWriter(new FileWriter(sb.toString(), true))) {
            sb = new StringBuilder();
            sb.append(String.format("%02d", dif.getHeureDiffusion().get(Calendar.HOUR_OF_DAY))).append(":")
                    .append(String.format("%02d", dif.getHeureDiffusion().get(Calendar.MINUTE))).append(":")
                    .append(String.format("%02d", dif.getHeureDiffusion().get(Calendar.SECOND))).append(" ")
                    .append(dif.getContrat().getIdVideo()).append("\n");
            output.append(sb.toString());
        }
    } 
    
    public ArrayList<Contrat> chargerContratsADiffuser() {
        ArrayList<Contrat> contrats_a_diffuser = new ArrayList();
        FileInputStream fis;
        try {
            fis = new FileInputStream("contrats");
            try (ObjectInputStream in = new ObjectInputStream(fis)) {
                contrats_a_diffuser = (ArrayList<Contrat>) in.readObject();
                in.close();
            }
            fis.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Contrat local non existant...");
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contrats_a_diffuser;
    }
    
    public void sauverContratsReportes(ArrayList<Contrat> contrats_reportes) {
        try {
            FileOutputStream fos;
            fos = new FileOutputStream("contrats_reportes");
            try (ObjectOutputStream out = new ObjectOutputStream(fos)) {
                out.writeObject(contrats_reportes);
                out.close();
            }
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<Contrat> chargerContratsReportes() {
        ArrayList<Contrat> contrats_reportes = new ArrayList();
        FileInputStream fis;
        try {
            fis = new FileInputStream("contrats_reportes");
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
        
    }
    
    FichierController(String path) {
        this.PATH = path;
    }

    FichierController(String path, String ip) {
        this.PATH = path;
        this.IP = ip;
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

    public ArrayList<Contrat> serLoad(String name) {
        ArrayList<Contrat> tcm = new ArrayList();
        FileInputStream fis;
        try {
            fis = new FileInputStream(PATH + name);
            try (ObjectInputStream in = new ObjectInputStream(fis)) {
                tcm = (ArrayList<Contrat>) in.readObject();
                in.close();
            }
            fis.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Contrat local non existant...");
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tcm;
    }

    public boolean serSave(String name, ArrayList<Contrat> tcm) {
        try {
            FileOutputStream fos;
            fos = new FileOutputStream(name); //name='contrats'
            try (ObjectOutputStream out = new ObjectOutputStream(fos)) {
                out.writeObject(tcm);
                out.close();
            }
            fos.close();
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
