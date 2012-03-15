/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hopto.seed419;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author seed419
 */
public class QuitListener implements Listener {


    private SprintWand sw;


    public QuitListener(SprintWand sw) {
        this.sw = sw;
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (sw.getWandActionList().containsKey(event.getPlayer())) {
            sw.getWandActionList().remove(event.getPlayer());
        }
    }

}
