/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hopto.seed419;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author SeeD419
 */
public class WandListener implements Listener {
    
    private SprintWand sw;
    private Settings settings;
    
    public WandListener(SprintWand sw, Settings settings) {
        this.sw = sw;
        this.settings = settings;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (sw.enabled() == true) {
                if (sw.getMemoryWands().get(player.getName()) != null) {
                    if (player.getItemInHand().getTypeId() == Integer.parseInt(sw.getMemoryWands().get(player.getName()).toString())) {
                        player.setSprinting(true);
                    }
                } else if (player.getItemInHand().getTypeId() == settings.getWandItem()) {
                    player.setSprinting(true);
                }
            }
        }
    }    
}
