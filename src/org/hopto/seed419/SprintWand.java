package org.hopto.seed419;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
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

//TODO Fix fall damage issue.
//TODO Write wand maps to file.

public class SprintWand extends JavaPlugin {


  private PluginManager pm;
  private WandListener listener;
  private PlayerDamageListener damageListener;
  private static PluginDescriptionFile pdf;
  private HashMap<String, HashMap<Integer, String>> memoryWands = new HashMap<String, HashMap<Integer,String>>();
  private HashMap<Player,Boolean> noFallDamage = new HashMap<Player,Boolean>();
//  private final String pluginDir = "plugins/SprintWand/";
//  private final String wandFilePath = "plugins/SprintWand/.wands";
  private File playerWandsFile = new File("plugins/SprintWand/.wands");
  private static final Logger log = Logger.getLogger("SprintWand");
  private String prefix = "[SW] " + ChatColor.GOLD;


    @Override
    public void onDisable() {
        log.log(Level.INFO, MessageFormat.format("{0} Disabled", new Object[] { pdf.getFullName() }));
    }


    @Override
    public void onEnable()  {
        this.damageListener = new PlayerDamageListener(this);
        this.listener = new WandListener(this);
        pdf = getDescription();
        this.pm = getServer().getPluginManager();
        this.pm.registerEvents(this.listener, this);
        this.pm.registerEvents(damageListener, this);
        new File("plugins/SprintWand/").mkdir();
        if (!this.playerWandsFile.exists()) {
            try {
                this.playerWandsFile.createNewFile();
            } catch (Exception ex) {
                log.log(Level.SEVERE, pdf.getName() + " unable to create wand storage file", ex);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if ((label.equalsIgnoreCase("sw")) || (label.equalsIgnoreCase("sprintwand"))) {
            if ((sender instanceof Player)) {
                Player player = (Player)sender;
                if (hasPerms(player)) {
                    if (args.length == 0) {
                        player.sendMessage(this.prefix + "Switches:");
                        player.sendMessage("    wand {arg}");
                        player.sendMessage("    mode {arg}");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("wand")) {
                        if (args.length == 1) {
                            player.sendMessage(this.prefix + "Usage: /sw wand {list}");
                            return true;
                        }
                        else if (args[1].equalsIgnoreCase("list")) {
                            if (memoryWands.containsKey(player.getName())) {
                                HashMap<Integer,String> temp = memoryWands.get(player.getName());
                                player.sendMessage(prefix + "Currently registered wands:");
                                for (Map.Entry<Integer,String> x : temp.entrySet()) {
                                    player.sendMessage("     " + ChatColor.DARK_AQUA +
                                            Material.getMaterial(x.getKey()).name().toLowerCase().replace("_", " ") + ChatColor.GOLD +
                                            " is set to " + ChatColor.DARK_AQUA +"'" + x.getValue() + "'");
                                }
                            } else {
                                player.sendMessage(prefix + "You don't appear to have any wands set..");
                            }

                        } else if (args[1].equalsIgnoreCase("help")) {
                            player.sendMessage(this.prefix + "Usage: /sw wand {list}");
                            return true;
                        } else {
                            player.sendMessage(this.prefix + "Invalid argument.  See /sw wand help");
                            return true;
                        }
                } else if (args[0].equalsIgnoreCase("mode")) {
                    if (args.length == 1) {
                        player.sendMessage(this.prefix + "Usage: /sw mode [arg]");
                        player.sendMessage(ChatColor.GOLD + "{high|higher|fast|sprint|suicide|faster}");
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("higher")) {
                        boolean success = setWand(player, "higher");
                        if (success) {
                            player.sendMessage(this.prefix + "This " + ChatColor.DARK_AQUA + player.getItemInHand().getType().name().toLowerCase().replace("_", " ")
                                + ChatColor.GOLD + " is set to 'higher' mode.");
                        }
                        return true;
                    } else if (args[1].equalsIgnoreCase("fast")) {
                        boolean success = setWand(player, "fast");
                        if (success) {
                            player.sendMessage(this.prefix + "This "+ ChatColor.DARK_AQUA + player.getItemInHand().getType().name().toLowerCase().replace("_", " ")
                                + ChatColor.GOLD + " is set to 'fast' mode.");
                        }
                        return true;
                    } else if (args[1].equalsIgnoreCase("sprint")) {
                        boolean success = setWand(player, "sprint");
                        if (success) {
                            player.sendMessage(this.prefix + "This "+ ChatColor.DARK_AQUA + player.getItemInHand().getType().name().toLowerCase().replace("_", " ")
                                + ChatColor.GOLD + " is set to 'sprint' mode.");
                        }
                        return true;
                    } else if (args[1].equalsIgnoreCase("faster")) {
                        boolean success = setWand(player, "faster");
                        if (success) {
                            player.sendMessage(this.prefix + "This "+ ChatColor.DARK_AQUA + player.getItemInHand().getType().name().toLowerCase().replace("_", " ")
                                + ChatColor.GOLD + " is set to 'faster' mode.");
                        }
                        return true;
                    } else if (args[1].equalsIgnoreCase("suicide")) {
                        boolean success = setWand(player, "suicide");
                        if (success) {
                            player.sendMessage(this.prefix + "This "+ ChatColor.DARK_AQUA + player.getItemInHand().getType().name().toLowerCase().replace("_", " ")
                                + ChatColor.GOLD + " is set to 'suicide' mode.");
                            player.sendMessage(this.prefix + ChatColor.GOLD + "Godspeed.");
                        }
                        return true;
                    } else if (args[1].equalsIgnoreCase("help")) {
                        player.sendMessage(this.prefix + "Usage: /sw mode [arg]");
                        player.sendMessage(ChatColor.GOLD + "{-high|-higher|-fast|-sprint|-suicide|-faster}");
                        return true;
                    } else if (args[1].equalsIgnoreCase("high")) {
                        boolean success = setWand(player, "high");
                        if (success) {
                            player.sendMessage(this.prefix + "This "+ ChatColor.DARK_AQUA + player.getItemInHand().getType().name().toLowerCase().replace("_", " ")
                                + ChatColor.GOLD + " is set to 'high' mode.");
                        }
                        return true;
                    } else if (args[1].equalsIgnoreCase("show")) {
                        if (memoryWands.get(player.getName()) != null) {
                            if (memoryWands.get(player.getName()).get(player.getItemInHand().getTypeId()) != null) {
                                player.sendMessage(prefix + "This " +
                                        player.getItemInHand().getType().name().toLowerCase().replace("_", " ")
                                        + " is set to " + memoryWands.get(player.getName()).get(player.getItemInHand().getTypeId()) +
                                        " mode,");
                                return true;
                            } else {
                                player.sendMessage(prefix + "This " + ChatColor.DARK_AQUA
                                        + player.getItemInHand().getType().name().toLowerCase().replace("_", " ") +
                                        ChatColor.GOLD + " currently doesn't a mode set.");
                                return true;
                            }
                        } else {
                            player.sendMessage(prefix + "You haven't set any wands yet.");
                        }
                    } else {
                        player.sendMessage(this.prefix +"Invalid argument.  See /sw mode help");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("help")) {
                    player.sendMessage(this.prefix + "Usage:  /sw {mode|wand} [arg]");
                    return true;
                } else {
                    player.sendMessage(this.prefix + "Invalid argument.  See /sw help");
                    return true;
                }
            }
            return false;
        }
        return false;
     }
     return false;
  }


    public boolean hasPerms(Player player) {
        return (player.hasPermission("SprintWand.wand")) || (player.isOp());
    }

    private boolean setWand(Player player, String mode) {
        if (player.getItemInHand().getType() == Material.AIR) {
            player.sendMessage(prefix + "It doesn't work unless you hold an item, sorry.");
            return false;
        }
        if (memoryWands.containsKey(player.getName())) {
            if (memoryWands.get(player.getName()).containsKey(player.getItemInHand().getTypeId())) {
                memoryWands.get(player.getName()).put(player.getItemInHand().getTypeId(), mode);
                return true;
            } else {
                memoryWands.get(player.getName()).put(player.getItemInHand().getTypeId(), mode);
                return true;
            }
        } else {
            HashMap<Integer,String> temp = new HashMap<Integer,String>();
            temp.put(player.getItemInHand().getTypeId(), mode);
            memoryWands.put(player.getName(), temp);
            return true;
        }
    }

    public PluginDescriptionFile getPdf() {
        return pdf;
    }

    public File getPlayerWandsFile() {
        return this.playerWandsFile;
    }

    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public HashMap<String, HashMap<Integer, String>> getMemoryWands() {
        return this.memoryWands;
    }

    public String getWandFilePath() {
        return "plugins/SprintWand/.wands";
    }

    public String getPrefix() {
        return prefix;
    }

    public void addNoFallDamage(Player player) {
        noFallDamage.put(player, Boolean.TRUE);
    }

    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public HashMap<Player,Boolean> getNoFallDamage() {
        return noFallDamage;
    }
}

//    public boolean writeWandsToFile() {
//        if (wandAction.size() > 0) {
//            try {
//                playerWandsFile.createNewFile();
//                BufferedWriter bw = new BufferedWriter(new FileWriter(playerWandsFile, false));
//                for (Map.Entry<Player,String> x : wandAction.entrySet()) {
//
//            }
//            } catch (IOException ex) {
//                Logger.getLogger(SprintWand.class.getName()).log(Level.SEVERE, "Couldn't create wand file!", ex);
//            }
//        }
//    }


