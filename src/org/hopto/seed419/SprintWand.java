/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hopto.seed419;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
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
    
    Logger log;
    PluginManager pm;
    Listener listener;
    Methods m;
    public static PluginDescriptionFile pdf;
    public static boolean isEnabled = true;
    public static boolean wtf = false;
    
    @Override
    public void onDisable() {
        log.log(Level.INFO, pdf.getFullName() + " " + " Disabled");
    }

    @Override
    public void onEnable() {
        pdf = getDescription();
        listener = new Listener();
        pm = this.getServer().getPluginManager();
        pm.registerEvent(Type.PLAYER_ANIMATION, listener, Priority.Low, this);
        m = new Methods();
        log = Logger.getLogger(pdf.getName());
        log.log(Level.INFO, pdf.getFullName() + " Enabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("sprint")){
           if(sender.hasPermission("SprintWand.wand")){
                if(sender instanceof Player){
                    Player player = (Player) sender;
                    m.toggleSprint(player);
                    return true;
                }else{
                    log.log(Level.INFO, "You must be a player to sprint :)");
                    return false;  
                    }
           }else{
                sender.sendMessage("You need the permission SprintWand.wand");
                return false;
                }
        }else if(label.equalsIgnoreCase("wtf")){
              if(sender instanceof Player){
                  Player player = (Player) sender;
                  m.wtf(player);
                  player.sendMessage(ChatColor.LIGHT_PURPLE+"wtf");
                  return true;
              }
          }
        return false;
        }   
}

