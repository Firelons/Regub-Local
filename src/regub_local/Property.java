package regub_local;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
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
    
    public String getDay() {
        Calendar cal = Calendar.getInstance();
        int dayOfMonth = cal.get(Calendar.DAY_OF_WEEK);
        switch(dayOfMonth) {
            case Calendar.MONDAY: return this.getProp("lundi");
            case Calendar.WEDNESDAY: return this.getProp("mercredi");
        }
        
        return null;
    }
}
