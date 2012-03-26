/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hopto.seed419;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 *
 * @author seed419
 */
public class PlayerDamageListener implements Listener {


    private SprintWand sw;


    public PlayerDamageListener(SprintWand sw) {
        this.sw = sw;
    }

    @EventHandler
    void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getCause() == DamageCause.FALL) {
                if (sw.getNoFallDamage().containsKey(player)) {
                    if (sw.getNoFallDamage().get(player)) {
                        event.setCancelled(true);
                        sw.getNoFallDamage().put(player, false);
                    }
                }
            }
        }
    }

}
