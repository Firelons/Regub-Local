package regub_local;

import java.io.*;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import static regub_local.ContratController.DB_URL;

public class FileController {

    private String IP = "http://172.16.0.50/";
    private String PATH = "VIDEOS/";

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

    public void delete(ArrayList<ContratModel> contrat) {
        Iterator<ContratModel> it = contrat.iterator();
        while (it.hasNext()) {
            ContratModel cm_local = it.next();
            File file = new File("VIDEOS/" + cm_local.idVideo + ".mp4");
            file.delete();
            System.out.println("!SUPPRIMER:" + cm_local.idVideo + ".MP4");
        }
    }

    public ArrayList<ContratModel> serLoad(String name) {
        ArrayList<ContratModel> tcm = new ArrayList();
        FileInputStream fis;
        try {
            fis = new FileInputStream(PATH + name);
            try (ObjectInputStream in = new ObjectInputStream(fis)) {
                tcm = (ArrayList<ContratModel>) in.readObject();
                in.close();
            }
            fis.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tcm;
    }

    public boolean serSave(String name, ArrayList<ContratModel> tcm) {
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
    
    public void setPath(String path) {
        this.PATH = path;
    }
}
