/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bg.plusplusnt.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefan
 */
public class SettingsLoader {

    private static SettingsLoader instance;
    private Properties databaseProperties;

    private SettingsLoader() {
        loadProperties();
    }

    public static SettingsLoader getInstance() {
        if (instance == null) {
            instance = new SettingsLoader();
        }
        return instance;
    }

    private void loadProperties() {
        try {
            databaseProperties = new Properties();
            databaseProperties.load(new FileReader(new File("database.properties")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SettingsLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SettingsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getDatabaseProperty(String key) {
        return databaseProperties.getProperty(key, "n/a");
    }
}
