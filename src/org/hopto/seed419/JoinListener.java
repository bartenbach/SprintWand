/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hopto.seed419;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author seed419
 */
public class JoinListener implements Listener {


    private SprintWand sw;


    public JoinListener(SprintWand sw) {
        this.sw = sw;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (sw.hasPerms(event.getPlayer()) || event.getPlayer().isOp()) {
            sw.getWandActionList().put(event.getPlayer(), "default");
        }
    }
}
