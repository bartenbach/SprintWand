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
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author SeeD419
 */
public class SprintWand extends JavaPlugin {


    private PluginManager pm;
    private WandListener listener;
    private JoinListener join;
    private QuitListener quit;
    private Methods m;
    private static PluginDescriptionFile pdf;
    private HashMap<String, Integer> MemoryWands = new HashMap<String, Integer>();
    private HashMap<Player, String> wandAction = new HashMap<Player, String>();
    private final String pluginDir = "plugins/SprintWand/";
    private final String wandFilePath = pluginDir + "PlayerWands.txt";
    private File  playerWandsFile = new File (wandFilePath);
    private static final Logger log = Logger.getLogger("SprintWand");
    private Settings settings;
    private String prefix = "[SW] ";


    @Override
    public void onDisable() {
        log.log(Level.INFO, MessageFormat.format("{0} Disabled", pdf.getFullName()));
    }

    @Override
    public void onEnable() {
        join = new JoinListener(this);
        quit = new QuitListener(this);
        settings = new Settings(this);
        listener = new WandListener(this, settings);
        m = new Methods(this, settings);
        pdf = getDescription();
        pm = this.getServer().getPluginManager();
        pm.registerEvents(quit, this);
        pm.registerEvents(join, this);
        pm.registerEvents(listener, this);
        new File(pluginDir).mkdir();

        if (!playerWandsFile.exists()) {
            try {
                playerWandsFile.createNewFile();
                log.log(Level.INFO, MessageFormat.format("{0} empty PlayerWands file created.", pdf.getName()));
            } catch(Exception ex) {
                log.log(Level.SEVERE, pdf.getName() + " unable to create PlayerWands file", ex);
            }
        }
        settings.loadConfiguration();
        m.writeWandsToMemory();
        log.log(Level.INFO, pdf.getName() + " loaded " + m.getLines() + " player wands into memory.");
        log.log(Level.INFO, pdf.getFullName() + " Loaded.  Current Global Wand: " + Material.getMaterial(settings.getWandItem()));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("sw") || label.equalsIgnoreCase("sprintwand")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (hasPerms(player)) {
                    if (args.length == 0) {
                        player.sendMessage(prefix + ChatColor.GOLD + "No switch supplied.  (See /sw -help)");
                        return true;
                    } else {
                        if (args[0].equalsIgnoreCase("wand")) {
                            if (args.length == 1) {
                                player.sendMessage(prefix + ChatColor.GOLD + "No argument supplied.  (See /sw wand -help)");
                                return true;
                            } else if (args[1].equalsIgnoreCase("-set")) {
                                int currentItem = m.getCurrentItem(player);
                                m.changeWandInMemory(player, currentItem);
                                player.sendMessage(prefix + ChatColor.GOLD + "Wand set to " + ChatColor.DARK_AQUA
                                        + Material.getMaterial(currentItem).name());
                                m.removeOldWand(player);
                                m.writeWandToFile(currentItem, player);
                                return true;
                            } else if (args[1].equalsIgnoreCase("-show")) {
                                if (MemoryWands.get(player.getName()) != null) {
                                    Material.getMaterial(Integer.parseInt(MemoryWands.get(player.getName()).toString()));
                                    player.sendMessage(prefix + ChatColor.GOLD + "Current wand: " + ChatColor.DARK_AQUA
                                        + Material.getMaterial(Integer.parseInt(MemoryWands.get(player.getName()).toString())));
                                    return true;
                                } else {
                                    player.sendMessage(prefix + ChatColor.GOLD + "Current wand: " + ChatColor.DARK_AQUA
                                            + Material.getMaterial(settings.getWandItem()));
                                    return true;
                                }
                            } else if (args[1].equalsIgnoreCase("-help")) {
                                player.sendMessage(prefix + ChatColor.GOLD + "Usage: /sw wand {-set|-show}");
                                return true;
                            } else {
                                player.sendMessage(prefix + ChatColor.GOLD + "Invalid argument.  See /sw wand -help");
                                return true;
                            }
                        } else if (args[0].equalsIgnoreCase("mode")) {
                            if (args.length == 1) {
                                player.sendMessage(prefix + ChatColor.GOLD + "No argument supplied.  (See /sw mode -help)");
                                return true;
                            } else if (args[1].equalsIgnoreCase("-higher")) {
                                wandAction.put(player, "up");
                                player.sendMessage(prefix + ChatColor.GOLD + "Wand changed to 'higher' mode.");
                                return true;
                            } else if (args[1].equalsIgnoreCase("-fast")) {
                                wandAction.put(player, "fast");
                                player.sendMessage(prefix + ChatColor.GOLD + "Wand changed to 'fast' mode.");
                                return true;
                            } else if (args[1].equalsIgnoreCase("-sprint")) {
                                wandAction.put(player, "default");
                                player.sendMessage(prefix + ChatColor.GOLD + "Wand changed to 'sprint' mode.");
                                return true;
                            } else if (args[1].equalsIgnoreCase("-faster")) {
                                wandAction.put(player, "faster");
                                player.sendMessage(prefix + ChatColor.GOLD + "Wand changed to 'faster' mode.");
                                return true;
                            } else if (args[1].equalsIgnoreCase("-suicide")) {
                                wandAction.put(player, "suicide");
                                player.sendMessage(prefix + ChatColor.GOLD + "Godspeed.");
                                return true;
                            } else if (args[1].equalsIgnoreCase("-help")) {
                                player.sendMessage(prefix + ChatColor.GOLD + "Usage: /sw mode [arg]");
                                player.sendMessage(ChatColor.GOLD + "{-high|-higher|-fast|-sprint|-suicide|-faster}");
                                return true;
                            } else if (args[1].equalsIgnoreCase("-high")) {
                                wandAction.put(player, "hop");
                                player.sendMessage(prefix + ChatColor.GOLD + "Wand changed to 'high' mode.");
                                return true;
                            } else {
                                player.sendMessage(prefix + ChatColor.GOLD + "Invalid argument.  See /sw mode --help");
                                return true;
                            }
                        } else if (args[0].equalsIgnoreCase("help")) {
                           player.sendMessage(prefix + ChatColor.GOLD + "Usage:  /sw {mode|wand} [arg]");
                           return true;
                        } else {
                           player.sendMessage(prefix + ChatColor.GOLD + "Invalid argument.  See /sw -help");
                           return true;
                        }
                    }
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public boolean hasPerms(Player player) {
        return (player.hasPermission("SprintWand.wand") || player.isOp());
    }

    public PluginDescriptionFile getPdf() {
        return pdf;
    }

    public File getPlayerWandsFile() {
        return playerWandsFile;
    }

    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public HashMap<String,Integer> getMemoryWands() {
        return MemoryWands;
    }

    public String getWandFilePath() {
        return wandFilePath;
    }

    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public HashMap<Player,String> getWandActionList() {
        return wandAction;
    }
}

