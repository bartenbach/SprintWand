/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hopto.seed419;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
    
           public static final Logger log = Logger.getLogger("SprintWand");
           public static int lines;
           
    public void toggleSprint(Player player){
        
        if(SprintWand.isEnabled == false){
           SprintWand.isEnabled = true;
           player.sendMessage(ChatColor.GOLD + SprintWand.pdf.getName() + " Enabled");
        }else{
           SprintWand.isEnabled = false;
           player.sendMessage(ChatColor.GOLD + SprintWand.pdf.getName() + " Disabled");
        }
    }
    
    public int parseArgs(String[] args, Player player){
        int wand = Integer.parseInt(args[0]);
        if(wand >= 0 && wand <= 2267){
            return wand;
        }else{
            player.sendMessage("Invalid Item Id.  SprintWand defaulted to stick.");
            return Settings.WandItem;
        }
    }

    void writeWandToFile(int wand, Player player) {
        try{
        BufferedWriter bf = new BufferedWriter(new FileWriter(SprintWand.playerWands, true));
        bf.write(player.getName() + ":" + wand);
        bf.newLine();
        bf.close();
        bf = null;
        System.gc(); 
            }catch(Exception ex){
                player.sendMessage("Unable to write wand to file");
                SprintWand.log.log(Level.SEVERE, SprintWand.pdf.getName() + " unable to write wand to file!");
            }
        }
    
    public static int getPlayerWandFromFile(Player player){
        int wandItem = Settings.WandItem;
        try{
            FileInputStream fs = new FileInputStream(SprintWand.wandFile);
            DataInputStream ds = new DataInputStream(fs);
            BufferedReader bf = new BufferedReader(new InputStreamReader(ds));
            String name = player.getName();
            String strLine;
            while ((strLine = bf.readLine()) != null){
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
            }catch(Exception ex){
            SprintWand.log.log(Level.SEVERE, SprintWand.pdf.getName() + " unable to read wands from file!", ex);
            player.sendMessage(ChatColor.GOLD + "Unable to read your wand from file :(");
            player.sendMessage(ChatColor.GOLD + "Your wand has been defaulted to the global wand: " + Material.getMaterial(Settings.WandItem));
            return Settings.WandItem;
        }

    }
    
    public static boolean playerAlreadyInFile(Player player){
        try{
            FileInputStream fs = new FileInputStream(SprintWand.playerWands);
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
            SprintWand.log.log(Level.SEVERE, SprintWand.pdf.getName() + " encountered an error reading PlayerWands.txt", ex);
        }
       return false;
    }
    
    static void removeOldWand(Player player) {
        String name = player.getName(); 
        try{
            File inFile = new File(SprintWand.wandFile);
            if(!inFile.isFile()){
            SprintWand.log.log(Level.SEVERE, SprintWand.pdf.getName() + " cannot locate " + inFile.getName());
            return;
            }
             File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
             BufferedReader br = new BufferedReader(new FileReader(inFile));
             PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
             String currentLine = null;
                  while ((currentLine = br.readLine()) != null){
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
                      if(!inFile.delete()){
                          SprintWand.log.log(Level.SEVERE, "Could not delete file!");
                      }
                      if(!tempFile.renameTo(inFile)){
                          SprintWand.log.log(Level.SEVERE, "Could not rename file!");
                      }
            }catch (FileNotFoundException ex){
                SprintWand.log.log(Level.SEVERE, SprintWand.pdf.getName() + " is unable to find file!", ex);
            }catch(IOException ex){
                SprintWand.log.log(Level.SEVERE, SprintWand.pdf.getName() + " has encountered an IO Exception", ex);   
            }
         }

    void writeWandsToMemory() {
        try{
            FileInputStream fs = new FileInputStream(SprintWand.playerWands);
            DataInputStream ds = new DataInputStream(fs);
            BufferedReader bf = new BufferedReader(new InputStreamReader(ds));
            String strLine;
            while((strLine = bf.readLine()) != null){
                String[] fileLine = strLine.split(":");
                if(fileLine.length == 2){
                    lines++;
                    String name = fileLine[0];
                    int itemID = Integer.parseInt(fileLine[1]);
                    SprintWand.MemoryWands.put(name, itemID);
                }
            }
                ds.close();
                ds = null;
                fs.close();
                fs = null;
                bf.close();
                bf = null;
                System.gc();
        }catch(Exception ex){
            SprintWand.log.log(Level.SEVERE, SprintWand.pdf.getName() + " encountered an error reading PlayerWands.txt", ex);
        }    }

    int getCurrentItem(Player player) {
        int currentItem = player.getItemInHand().getTypeId();
        return currentItem;
    }
    
    int getWandFromMemory(Player player){
        String name = player.getName();
        if(SprintWand.MemoryWands.containsKey(name)){
            String wand = SprintWand.MemoryWands.get(name).toString();
            int item = Integer.parseInt(wand);
            return item;
        }
        return Settings.WandItem;
    }

    void changeWandInMemory(Player player, int item) {
        String name = player.getName();
        if(SprintWand.MemoryWands.containsKey(name)){
            SprintWand.MemoryWands.remove(name);
        }
        SprintWand.MemoryWands.put(name, item);
    }
    
  }