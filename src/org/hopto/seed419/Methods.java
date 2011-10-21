/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hopto.seed419;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 *
 * @author SeeD419
 */
public class Methods {
        
    public void toggleSprint(Player player){
        
        if(SprintWand.isEnabled == false){
           SprintWand.isEnabled = true;
           player.sendMessage(ChatColor.GOLD + SprintWand.pdf.getName() + " Enabled");
        }else{
           SprintWand.isEnabled = false;
           player.sendMessage(ChatColor.GOLD + SprintWand.pdf.getName() + " Disabled");
        }
    }
    
    public void wtf(Player player){
        player.setVelocity(Vector.getRandom());
    }

}