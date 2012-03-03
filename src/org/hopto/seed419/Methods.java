/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hopto.seed419;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
/**
 *
 * @author SeeD419
 */
public class Methods {
    
    
    private static final Logger log = Logger.getLogger("SprintWand");
    private int lines;
    private SprintWand sw;
    private Settings settings;
    
           
    public Methods(SprintWand sw, Settings settings) {
        this.sw = sw;
        this.settings = settings;
    }
           
    public void toggleSprint(Player player){
        
        if (sw.enabled() == false) {
           sw.setEnable(true);
           player.sendMessage(ChatColor.GOLD + sw.getPdf().getName() + " Enabled");
        } else {
           sw.setEnable(false);
           player.sendMessage(ChatColor.GOLD + sw.getPdf().getName() + " Disabled");
        }
    }
    
    public int parseArgs(String[] args, Player player){
        int wand = Integer.parseInt(args[0]);
        if(wand >= 0 && wand <= 2267){
            return wand;
        }else{
            player.sendMessage("Invalid Item Id.  SprintWand defaulted to stick.");
            return settings.getWandItem();
        }
    }

    public void writeWandToFile(int wand, Player player) {
        try{
        BufferedWriter bf = new BufferedWriter(new FileWriter(sw.getPlayerWandsFile(), true));
        bf.write(player.getName() + ":" + wand);
        bf.newLine();
        bf.close();
        bf = null;
        System.gc(); 
            }catch(Exception ex){
                player.sendMessage("Unable to write wand to file");
                log.log(Level.SEVERE, sw.getPdf().getName() + " unable to write wand to file!");
            }
        }
    
    public int getPlayerWandFromFile(Player player) {
        int wandItem = settings.getWandItem();
        try {
            FileInputStream fs = new FileInputStream(sw.getPlayerWandsFile());
            DataInputStream ds = new DataInputStream(fs);
            BufferedReader bf = new BufferedReader(new InputStreamReader(ds));
            String name = player.getName();
            String strLine;
            while ((strLine = bf.readLine()) != null) {
                    if (strLine.startsWith(name)){
                        String[] fileLine = strLine.split(":");
                        int counter = 0;
                            for(String f : fileLine){
                            counter++;
                        }
                   counter-=1;
                   String wandString = fileLine[counter];
                   wandItem = Integer.parseInt(wandString);
                   }
                }
                ds.close();
                ds = null;
                fs.close();
                fs = null;
                bf.close();
                bf = null;
                System.gc();
            return wandItem;
        } catch(Exception ex) {
            log.log(Level.SEVERE, sw.getPdf().getName() + " unable to read wands from file!", ex);
            player.sendMessage(ChatColor.GOLD + "Unable to read your wand from file :(");
            player.sendMessage(ChatColor.GOLD + "Your wand has been defaulted to the global wand: " + Material.getMaterial(settings.getWandItem()));
            return settings.getWandItem();
        }

    }
    
    public boolean playerAlreadyInFile(Player player) {
        try {
            FileInputStream fs = new FileInputStream(sw.getPlayerWandsFile());
            DataInputStream ds = new DataInputStream(fs);
            BufferedReader bf = new BufferedReader(new InputStreamReader(ds));
            String name = player.getName();
            String strLine;
            while((strLine = bf.readLine()) != null){
                if(strLine.startsWith(name)){
                    return true;
                }
            }
                ds.close();
                ds = null;
                fs.close();
                fs = null;
                bf.close();
                bf = null;
                System.gc();
            return false;
        }catch(Exception ex){
            log.log(Level.SEVERE, sw.getPdf().getName() + " encountered an error reading PlayerWands.txt", ex);
        }
       return false;
    }
    
    public void removeOldWand(Player player) {
        String name = player.getName(); 
        try {
            File inFile = sw.getPlayerWandsFile();
            if (!inFile.isFile()) {
                log.log(Level.SEVERE, sw.getPdf().getName() + " cannot locate " + inFile.getName());
            return;
            }
             File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
             BufferedReader br = new BufferedReader(new FileReader(inFile));
             PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
             String currentLine = null;
                  while ((currentLine = br.readLine()) != null) {
                      String trimmedLine = currentLine.trim();
                      if (!trimmedLine.startsWith(name)) {
                          pw.println(trimmedLine);
                          pw.flush();    
                          }
                      }
                      pw.close();
                      pw.flush();
                      pw = null;
                      br.close();
                      br = null;
                      System.gc();
                      if(!inFile.delete()) {
                          log.log(Level.SEVERE, "Could not delete file!");
                      }
                      if(!tempFile.renameTo(inFile)) {
                          log.log(Level.SEVERE, "Could not rename file!");
                      }
            } catch (FileNotFoundException ex) {
                log.log(Level.SEVERE, sw.getPdf().getName() + " is unable to find file!", ex);
            } catch(IOException ex) {
                log.log(Level.SEVERE, sw.getPdf().getName() + " has encountered an IO Exception", ex);   
            }
         }

    public void writeWandsToMemory() {
        try{
            FileInputStream fs = new FileInputStream(sw.getPlayerWandsFile());
            DataInputStream ds = new DataInputStream(fs);
            BufferedReader bf = new BufferedReader(new InputStreamReader(ds));
            String strLine;
            while((strLine = bf.readLine()) != null){
                String[] fileLine = strLine.split(":");
                if(fileLine.length == 2){
                    lines++;
                    String name = fileLine[0];
                    int itemID = Integer.parseInt(fileLine[1]);
                    sw.getMemoryWands().put(name, itemID);
                }
            }
                ds.close();
                ds = null;
                fs.close();
                fs = null;
                bf.close();
                bf = null;
                System.gc();
        } catch(Exception ex) {
            log.log(Level.SEVERE, sw.getPdf().getName() + " encountered an error reading PlayerWands.txt", ex);
        }  
    }

    public int getCurrentItem(Player player) {
        int currentItem = player.getItemInHand().getTypeId();
        return currentItem;
    }
    
    public int getWandFromMemory(Player player){
        String name = player.getName();
        if (sw.getMemoryWands().containsKey(name)) {
            String wand = sw.getMemoryWands().get(name).toString();
            int item = Integer.parseInt(wand);
            return item;
        }
        return settings.getWandItem();
    }

    public void changeWandInMemory(Player player, int item) {
        String name = player.getName();
        if(sw.getMemoryWands().containsKey(name)){
            sw.getMemoryWands().remove(name);
        }
        sw.getMemoryWands().put(name, item);
    }
    
    public int getLines() {
        return lines;
    }
    
  }