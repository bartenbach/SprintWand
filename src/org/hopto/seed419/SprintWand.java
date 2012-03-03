/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hopto.seed419;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author SeeD419
 */
public class SprintWand extends JavaPlugin{
    
    private PluginManager pm;
    private WandListener listener;
    private Methods m;
    private static PluginDescriptionFile pdf;
    private  boolean enabled = true;
    private HashMap<String, Integer> MemoryWands = new HashMap<String, Integer>();
    private final String pluginDir = "plugins/SprintWand/";
    private final String wandFilePath = pluginDir + "PlayerWands.txt";
    private final String tempFilePath = pluginDir + "temp.tmp";
    private File  playerWandsFile = new File (wandFilePath);
    private static final Logger log = Logger.getLogger("SprintWand");
    private Settings settings;
    
    @Override
    public void onDisable() {
        log.log(Level.INFO, MessageFormat.format("{0} Disabled", pdf.getFullName()));
    }

    @Override
    public void onEnable() {
        settings = new Settings(this);
        listener = new WandListener(this, settings);
        m = new Methods(this, settings);        
        pdf = getDescription();
        pm = this.getServer().getPluginManager();
        pm.registerEvents(listener, this);
        new File(pluginDir).mkdir();
        
        if (!playerWandsFile.exists()) {
            try {
                playerWandsFile.createNewFile();
                log.log(Level.INFO, MessageFormat.format("{0} empty PlayerWands file created.", pdf.getName()));
            } catch(Exception ex) {
                log.log(Level.SEVERE, pdf.getName() + " unable to create PlayerWands file", ex);
            }
        }
        settings.loadConfiguration();
        m.writeWandsToMemory();
        log.log(Level.INFO, pdf.getName() + " loaded " + m.getLines() + " player wands into memory.");
        log.log(Level.INFO, pdf.getFullName() + " Loaded.  Current Global Wand: " + Material.getMaterial(settings.getWandItem()));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("sprint")) {
           if (sender instanceof Player) {
                if (sender.hasPermission("SprintWand.wand")) {
                    Player player = (Player) sender;
                    m.toggleSprint(player);
                    return true;
                } else {
                    sender.sendMessage("You need the permission SprintWand.wand");
                    return false;  
                    }
           } else {
                log.log(Level.INFO, "You must be a player to sprint :)");   
                return false;
                }
        } else if (label.equalsIgnoreCase("setwandto")) {
            if (sender instanceof Player) {
                if (sender.hasPermission("SprintWand.wand")) {
                    Player player = (Player) sender;
                        if (m.playerAlreadyInFile(player)) {
                            m.removeOldWand(player);
                            int wand = m.parseArgs(args, player);
                            m.writeWandToFile(wand, player);
                            m.changeWandInMemory(player, wand);
                            player.sendMessage(ChatColor.GOLD + "SprintWand Changed to " + ChatColor.DARK_AQUA + Material.getMaterial(wand).name());
                            return true;                            
                        } else {     
                            int wand = m.parseArgs(args, player);
                            m.writeWandToFile(wand, player);
                            m.changeWandInMemory(player, wand);
                            player.sendMessage(ChatColor.GOLD + "SprintWand Changed to " + ChatColor.DARK_AQUA + Material.getMaterial(wand).name());
                            return true;
                        }
                } else {
                    sender.sendMessage("You need the permission SprintWand.wand");
                    return false;
                }
            } else {
                log.log(Level.INFO, "You must be a player to sprint :)");
                return false;
            }
        } else if (label.equalsIgnoreCase("wand")) { 
            if (sender instanceof Player) {
                if (sender.hasPermission("SprintWand.wand")) {
                    Player player = (Player) sender;
                    if (MemoryWands.get(player.getName()) != null) {
                        Material.getMaterial(Integer.parseInt(MemoryWands.get(player.getName()).toString()));
                    player.sendMessage(ChatColor.GOLD + "Current wand: " + ChatColor.DARK_AQUA + Material.getMaterial(Integer.parseInt(MemoryWands.get(player.getName()).toString())));
                    } else {
                       player.sendMessage(ChatColor.GOLD + "Current wand: " + ChatColor.DARK_AQUA + Material.getMaterial(settings.getWandItem()));
                    }
                    return true;
                } else {
                     sender.sendMessage("You need the permission SprintWand.wand");
                     return false;
                }
            } else {
                log.log(Level.INFO, "You must be a player to sprint :)");
                return false;                
            }
        } else if (label.equalsIgnoreCase("setwand")) {
            if (sender instanceof Player) {
                if (sender.hasPermission("SprintWand.wand")) {
                    Player player = (Player) sender;
                    int currentItem = m.getCurrentItem(player);
                    m.changeWandInMemory(player, currentItem);
                    player.sendMessage(ChatColor.GOLD + "SprintWand Changed to " + ChatColor.DARK_AQUA + Material.getMaterial(currentItem).name());
                    m.removeOldWand(player);
                    m.writeWandToFile(currentItem, player);
                }
            }
        }
        return false;
     }
    
    public boolean enabled() {
        return enabled;
    }
    
    public void setEnable(boolean b) {
        enabled = b;
    }
    
    public PluginDescriptionFile getPdf() {
        return pdf;
    }
    
    public File getPlayerWandsFile() {
        return playerWandsFile;
    }
    
    public HashMap<String,Integer> getMemoryWands() {
        return MemoryWands;
    }
    
    public String getWandFilePath() {
        return wandFilePath;
    }
}

