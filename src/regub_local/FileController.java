package regub_local;

import java.io.*;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileController {

    private String IP;
    private String PATH;

    FileController(String path) {
        this.PATH = path;
    }

    FileController(String path, String ip) {
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
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(ArrayList<Contrat> contrat) {
        Iterator<Contrat> it = contrat.iterator();
        while (it.hasNext()) {
            Contrat cm_local = it.next();
            String path = PATH + cm_local.idVideo + ".mp4";
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
