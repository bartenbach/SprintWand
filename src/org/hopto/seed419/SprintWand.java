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
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author SeeD419
 */
public class SprintWand extends JavaPlugin{
    
    PluginManager pm;
    Listener listener;
    Methods m;
    public static PluginDescriptionFile pdf;
    public static boolean isEnabled = true;
    public static HashMap MemoryWands = new HashMap();
    static final String pluginDir = "plugins/SprintWand/";
    static final String wandFile = pluginDir + "PlayerWands.txt";
    static final String tempFile = pluginDir + "temp.tmp";
    static File  playerWands = new File (wandFile);
    //static File config = new File(pluginDir + "config.txt");
    public static final Logger log = Logger.getLogger("SprintWand");
    
    @Override
    public void onDisable() {
        log.log(Level.INFO, MessageFormat.format("{0} Disabled", pdf.getFullName()));
    }

    @Override
    public void onEnable() {
        pdf = getDescription();
        listener = new Listener();
        pm = this.getServer().getPluginManager();
        pm.registerEvent(Type.PLAYER_INTERACT, listener, Priority.Low, this);
        m = new Methods();
        new File(pluginDir).mkdir();
        //new File(tempDir).mkdir();
        if(!playerWands.exists()){
            try{
                playerWands.createNewFile();
                log.log(Level.INFO, MessageFormat.format("{0} empty PlayerWands file created.", pdf.getName()));
            }catch(Exception ex){
                log.log(Level.SEVERE, pdf.getName() + " unable to create PlayerWands file", ex);
            }
        }
        Settings.loadConfiguration();
        m.writeWandsToMemory();
        log.log(Level.INFO, pdf.getName() + " loaded " + Methods.lines + " player wands into memory.");
        log.log(Level.INFO, pdf.getFullName() + " Loaded.  Current Global Wand: " + Material.getMaterial(Settings.WandItem));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("sprint")){
           if(sender instanceof Player){
                if(sender.hasPermission("SprintWand.wand")){
                    Player player = (Player) sender;
                    m.toggleSprint(player);
                    return true;
                }else{
                    sender.sendMessage("You need the permission SprintWand.wand");
                    return false;  
                    }
           }else{
                log.log(Level.INFO, "You must be a player to sprint :)");   
                return false;
                }
        }else if(label.equalsIgnoreCase("setwandto")){
            if(sender instanceof Player){
                if(sender.hasPermission("SprintWand.wand")){
                    Player player = (Player) sender;
                        if(Methods.playerAlreadyInFile(player)){
                            Methods.removeOldWand(player);
                            int wand = m.parseArgs(args, player);
                            m.writeWandToFile(wand, player);
                            m.changeWandInMemory(player, wand);
                            player.sendMessage(ChatColor.GOLD + "SprintWand Changed to " + ChatColor.DARK_AQUA + Material.getMaterial(wand).name());
                            return true;                            
                        }else{     
                            int wand = m.parseArgs(args, player);
                            m.writeWandToFile(wand, player);
                            m.changeWandInMemory(player, wand);
                            player.sendMessage(ChatColor.GOLD + "SprintWand Changed to " + ChatColor.DARK_AQUA + Material.getMaterial(wand).name());
                            return true;
                        }
                }else{
                    sender.sendMessage("You need the permission SprintWand.wand");
                    return false;
                }
            }else{
                log.log(Level.INFO, "You must be a player to sprint :)");
                return false;
            }
        }else if(label.equalsIgnoreCase("wand")){
            if(sender instanceof Player){
                if(sender.hasPermission("SprintWand.wand")){
                    Player player = (Player) sender;
                    if(MemoryWands.get(player.getName()) != null){
                        Material.getMaterial(Integer.parseInt(MemoryWands.get(player.getName()).toString()));
                    player.sendMessage(ChatColor.GOLD + "Current wand: " + ChatColor.DARK_AQUA + Material.getMaterial(Integer.parseInt(MemoryWands.get(player.getName()).toString())));
                    }else{
                       player.sendMessage(ChatColor.GOLD + "Current wand: " + ChatColor.DARK_AQUA + Material.getMaterial(Settings.WandItem));
                    }
                    return true;
                }else{
                     sender.sendMessage("You need the permission SprintWand.wand");
                     return false;
                }
            }else{
                log.log(Level.INFO, "You must be a player to sprint :)");
                return false;                
            }
        }else if(label.equalsIgnoreCase("setwand")){
            if(sender instanceof Player){
                if(sender.hasPermission("SprintWand.wand")){
                    Player player = (Player) sender;
                    int currentItem = m.getCurrentItem(player);
                    m.changeWandInMemory(player, currentItem);
                    player.sendMessage(ChatColor.GOLD + "SprintWand Changed to " + ChatColor.DARK_AQUA + Material.getMaterial(currentItem).name());
                    Methods.removeOldWand(player);
                    m.writeWandToFile(currentItem, player);
                }
            }
        }
        return false;
     }
}

