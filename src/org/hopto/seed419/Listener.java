/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hopto.seed419;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

/**
 *
 * @author SeeD419
 */
public class Listener extends PlayerListener{

    @Override
    public void onPlayerAnimation(PlayerAnimationEvent event) {   
        Player player = event.getPlayer();
        if(event.getAnimationType() == PlayerAnimationType.ARM_SWING){
            if(SprintWand.isEnabled == true){
                if(player.getItemInHand().getTypeId() == Methods.getPlayerWand(player)){
                player.setSprinting(true);
                }else{

               
                }
             }
          }
       }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getAction() == Action.RIGHT_CLICK_AIR)
            if(SprintWand.isEnabled == true){
                if(player.getItemInHand().getTypeId() == Methods.getPlayerWand(player)){
                player.setSprinting(true);
                }
            }
        }
    }
