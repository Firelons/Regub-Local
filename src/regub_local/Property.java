package regub_local;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.util.Calendar;
import java.util.Properties;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vinc
 */
public class Property {
    protected Properties prop;
    
    public Property() throws IOException {
        prop = new Properties();
        String propFileName = "config.properties";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        
        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }
    }
    
    public String getProp(String name) {
        return prop.getProperty(name);
    }
    
    public Time[] getHours() {
        Calendar cal = Calendar.getInstance();
        int dayOfMonth = cal.get(Calendar.DAY_OF_WEEK);
        Time time[] = null;
        String[] filtre;
        String[] filtre_open;
        String[] filtre_close;
        switch(dayOfMonth) {
            case Calendar.MONDAY:
                filtre = this.getProp("lundi").split("-");
                filtre_open = filtre[0].split("h");
                time[0] = new Time(Integer.parseInt(filtre_open[0]), Integer.parseInt(filtre_open[1]), 0);
                filtre_close = filtre[1].split("h");
                time[1] = new Time(Integer.parseInt(filtre_close[0]), Integer.parseInt(filtre_close[1]), 0);
                break;
            case Calendar.TUESDAY:
                filtre = this.getProp("mardi").split("-");
                filtre_open = filtre[0].split("h");
                time[0] = new Time(Integer.parseInt(filtre_open[0]), Integer.parseInt(filtre_open[1]), 0);
                filtre_close = filtre[1].split("h");
                time[1] = new Time(Integer.parseInt(filtre_close[0]), Integer.parseInt(filtre_close[1]), 0);
                break;
            case Calendar.WEDNESDAY:
                filtre = this.getProp("mercredi").split("-");
                filtre_open = filtre[0].split("h");
                time[0] = new Time(Integer.parseInt(filtre_open[0]), Integer.parseInt(filtre_open[1]), 0);
                filtre_close = filtre[1].split("h");
                time[1] = new Time(Integer.parseInt(filtre_close[0]), Integer.parseInt(filtre_close[1]), 0);
                break;
        }
        return time;
    }
}
