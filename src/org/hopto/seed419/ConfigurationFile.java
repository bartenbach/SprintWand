/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hopto.seed419;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SeeD419
 */
public class ConfigurationFile extends Properties {
    static int WandItem;
    private String fileName;
    private Properties properties;
    private static final Logger log = Logger.getLogger("SprintWand");
    
    public ConfigurationFile(String fileName){
        this.fileName = fileName;
        this.properties = new Properties();
    }

    public void load(){
        File file = new File(this.fileName);
        if(file.exists()){
            try{
                load(new FileInputStream(this.fileName));
            }catch(IOException ex){
                log.log(Level.SEVERE, "Unable to load " + fileName, ex);
            }
        }
    }
    
    public int getInt(String key, int value){
        if(containsKey(key)){
           return Integer.parseInt(getProperty(key));
        }else{
           put(key, String.valueOf(value));
           return value;
        }   
    }
    
    public void save(){
        try{
            FileOutputStream output = new FileOutputStream(fileName);
            this.properties.store(output, fileName);
            output.close();
        }catch(IOException ex){
            log.log(Level.SEVERE, "Unable to save " + this.fileName + "!", ex);
        }
    }
    
    public void save(String start){
        try{
            FileOutputStream output = new FileOutputStream(this.fileName);
            store(output, start);
            output.close();
        }catch(IOException ex){
            log.log(Level.SEVERE, "Unable to save " + this.fileName + "!", ex);
        }
    }
    
    
    
}
