/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hopto.seed419;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

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
            if (sw.hasPerms(player)) {
                if (sw.getMemoryWands().get(player.getName()) != null) {
                    if (player.getItemInHand().getTypeId() == Integer.parseInt(sw.getMemoryWands().get(player.getName()).toString())) {
                        if (player.getFoodLevel() > 6) {
                            //This was null:
                            if (sw.getWandActionList().get(player).equalsIgnoreCase("default") && !player.isSprinting()) {
                                player.setSprinting(false);
                                player.setSprinting(true);
                            } else if (sw.getWandActionList().get(player).equalsIgnoreCase("higher")) {
                                Vector dir = player.getLocation().getDirection();
                                Vector newv = new Vector(dir.getX(), dir.getY() + 2, dir.getZ());
                                player.setVelocity(newv);
                                player.setFallDistance(player.getFallDistance() - 50);
                            } else if (sw.getWandActionList().get(player).equalsIgnoreCase("fast")) {
                                Vector dir = player.getLocation().getDirection();
                                Vector newv = new Vector(dir.getX() * 3.5, dir.getY() +.3, dir.getZ() * 3.5);
                                player.setVelocity(newv);
                                player.setFallDistance(player.getFallDistance() - 50);
                            } else if (sw.getWandActionList().get(player).equalsIgnoreCase("suicide")) {
                                Vector dir = player.getLocation().getDirection();
                                Vector newv = new Vector(dir.getX() * 50, dir.getY() + 10000, dir.getZ() * 50);
                                player.setVelocity(newv);
                            } else if (sw.getWandActionList().get(player).equalsIgnoreCase("high")) {
                                Vector dir = player.getLocation().getDirection();
                                Vector newv = new Vector(dir.getX(), dir.getY() + 1, dir.getZ());
                                player.setVelocity(newv);
                                player.setFallDistance(player.getFallDistance() - 50);
                            } else if (sw.getWandActionList().get(player).equalsIgnoreCase("faster")) {
                                Vector dir = player.getLocation().getDirection();
                                Vector newv = new Vector(dir.getX() * 10, dir.getY() +.6, dir.getZ() * 10);
                                player.setVelocity(newv);
                                player.setFallDistance(player.getFallDistance() - 50);
                            }
                        } else {
                            player.sendMessage(ChatColor.GOLD + "You're too hungry to sprint.  Eat something first!");
                        }
                    }
                } else if (player.getItemInHand().getTypeId() == settings.getWandItem()) {
                    if (player.getFoodLevel() > 6) {
                        player.setSprinting(false);
                        player.setSprinting(true);
                        player.sendMessage(ChatColor.GOLD + "Whoosh!");
                    } else {
                        player.sendMessage(ChatColor.GOLD + "You're too hungry to sprint.  Eat something first!");
                    }
                }
            }
        }
    }
}
