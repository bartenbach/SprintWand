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
    
    
    private int WandItem;
    private SprintWand sw;
    
    
    public Settings(SprintWand sw) {
        this.sw = sw;
    }
    
    public void loadConfiguration(){
        String configurationFile = sw.getWandFilePath() + "SprintWand.properties";
        ConfigurationFile config = new ConfigurationFile(configurationFile);
        config.load();
        
        WandItem = config.getInt("GlobalWandItem", 280);
        
        config.save("========= SprintWand Configuration =========");
    }
    
    public int getWandItem() {
        return WandItem;
    }
    
}
