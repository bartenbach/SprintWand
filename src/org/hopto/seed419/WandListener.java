/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hopto.seed419;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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


    public WandListener(SprintWand sw) {
        this.sw = sw;
    }

    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (sw.hasPerms(player)) {
                if (sw.getMemoryWands().get(player.getName()) != null) {
                        if (player.getFoodLevel() > 6) {
                            Integer item = player.getItemInHand().getTypeId();
                            if (!sw.getMemoryWands().get(player.getName()).containsKey(item)) {
                                return;
                            }
                            if (sw.getMemoryWands().get(player.getName()).get(item).equalsIgnoreCase("sprint")) {
                                player.setSprinting(true);
                            } else if (sw.getMemoryWands().get(player.getName()).get(item).equalsIgnoreCase("higher")) {
                                Vector dir = player.getLocation().getDirection();
                                Vector newv = new Vector(dir.getX(), dir.getY() + 2, dir.getZ());
                                player.setVelocity(newv);
                                sw.addNoFallDamage(player);
                            } else if (sw.getMemoryWands().get(player.getName()).get(item).equalsIgnoreCase("fast")) {
                                Vector dir = player.getLocation().getDirection();
                                Vector newv = new Vector(dir.getX() * 3.5, dir.getY() +.3, dir.getZ() * 3.5);
                                player.setVelocity(newv);
                                sw.addNoFallDamage(player);
                            } else if (sw.getMemoryWands().get(player.getName()).get(item).equalsIgnoreCase("suicide")) {
                                Vector dir = player.getLocation().getDirection();
                                Vector newv = new Vector(dir.getX() * 50, dir.getY() + 10000, dir.getZ() * 50);
                                sw.addNoFallDamage(player);
                            } else if (sw.getMemoryWands().get(player.getName()).get(item).equalsIgnoreCase("high")) {
                                Vector dir = player.getLocation().getDirection();
                                Vector newv = new Vector(dir.getX(), dir.getY() + 1, dir.getZ());
                                player.setVelocity(newv);
                                sw.addNoFallDamage(player);
                            } else if (sw.getMemoryWands().get(player.getName()).get(item).equalsIgnoreCase("faster")) {
                                Vector dir = player.getLocation().getDirection();
                                Vector newv = new Vector(dir.getX() * 10, dir.getY() +.6, dir.getZ() * 10);
                                player.setVelocity(newv);
                                sw.addNoFallDamage(player);
                            }
                        } else {
                            player.sendMessage(sw.getPrefix() +
                                    ChatColor.GOLD + "You're too hungry to use the wand.  Eat something first!");
                        }
                    }
                }
            }
        }
}

