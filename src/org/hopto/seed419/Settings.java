/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hopto.seed419;

/**
 *
 * @author SeeD419
 */
public class Settings {
    static int WandItem;
    
    public static void loadConfiguration(){
        String configurationFile = SprintWand.pluginDir + "SprintWand.properties";
        ConfigurationFile config = new ConfigurationFile(configurationFile);
        config.load();
        
        WandItem = config.getInt("GlobalWandItem", 280);
        
        config.save("========= SprintWand Configuration =========");
    }
    
}
