package regub_local;

import java.io.*;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Download {

    static final String IP = "http://172.16.0.50/";
    
   public static void main(String[] args) {
       getFile("test.mp4");
   }
   
   public static void getFile(String name) {
       URL website;
        try {
            website = new URL(IP + name);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream("VIDEOS/"+name);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
}