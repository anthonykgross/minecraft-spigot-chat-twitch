package fr.anthonykgross.TwitchChatPlugin;

import fr.anthonykgross.TwitchChatPlugin.Command.BountyCommand;
import fr.anthonykgross.TwitchChatPlugin.Model.Board;
import fr.anthonykgross.TwitchChatPlugin.Model.Bounty;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class App {
    private JavaPlugin plugin;
    private Board board;

    public App(JavaPlugin plugin) {
        this.plugin = plugin;
        this.board = new Board();
    }

    public Server getServer() {
        return this.plugin.getServer();
    }


    public void onPlayerJoin(PlayerJoinEvent p) {
        this.refreshGlowing();
    }

    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = (Player) e.getEntity();
        Player hunter = player.getKiller();

        int index = 0;
        for (Iterator<Bounty> iterator = this.board.iterator(); iterator.hasNext(); ) {
            Bounty bounty = iterator.next();

            if (bounty.getFugitive().equals(player)) {
                if (hunter != null) {
                    this.giveItem(hunter, bounty.getMaterial(), bounty.getAmount());
                    this.refreshGlowing();

                    iterator.remove();
                    Bukkit.broadcastMessage(
                            ChatColor.YELLOW + hunter.getDisplayName()
                                    + ChatColor.GREEN + " completed the Bounty"
                                    + ChatColor.YELLOW + " #" + index
                    );
                }
            }
            index++;
        }
    }

    public boolean onBountyCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        boolean commandOk = false;

        if (strings.length > 0) {
            String action = strings[0];
            switch (BountyCommand.getAction(action)) {
                case BountyCommand.ACTION_ADD:
                    commandOk = this.onBountyAddCommand(commandSender, command, s, strings);
                    break;
                case BountyCommand.ACTION_LIST:
                    commandOk = this.onBountyListCommand(commandSender, command, s, strings);
                    break;
                case BountyCommand.ACTION_RM:
                    commandOk = this.onBountyRmCommand(commandSender, command, s, strings);
                    break;
            }
        }
        if (!commandOk) {
            commandOk = this.onBountyHelpCommand(commandSender, command, s, strings);
        }
        return commandOk;
    }

    public boolean onBountyHelpCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            player.sendMessage(
                    ChatColor.YELLOW + "/bounty add <username> <amount> <item> \n"
                            + "/bounty list \n"
                            + "/bounty rm <bounty_number> \n"
                            + "/bounty help \n"
            );
        }
        return true;
    }

    public boolean onBountyAddCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player employer = (Player) commandSender;

            Player fugitive = null;
            Integer reward = null;
            Material material = null;

            boolean argsAreValid = true;

            if (strings.length == 4) {
                fugitive = this.getServer().getPlayer(strings[1]);

                if (fugitive == null) {
                    employer.sendMessage(
                            ChatColor.RED + "Player "
                                    + ChatColor.YELLOW + strings[1]
                                    + ChatColor.RED + " doesn't exist or isn't online."
                    );
                    argsAreValid = false;
                }

                try {
                    reward = Integer.parseInt(strings[2]);
                } catch (Exception e) {
                    employer.sendMessage(
                            ChatColor.YELLOW + "<amount>"
                                    + ChatColor.RED + " has to be an Integer. "
                                    + ChatColor.YELLOW + strings[2]
                                    + ChatColor.RED + " given"
                    );
                    argsAreValid = false;
                }

                material = Material.getMaterial(strings[3].toUpperCase());
                if (material == null) {
                    employer.sendMessage(
                            ChatColor.RED + "Unable to find constant "
                                    + ChatColor.YELLOW + "Material."
                                    + strings[3]
                    );
                    argsAreValid = false;
                }
            } else {
                employer.sendMessage(
                        ChatColor.RED + "This command require 4 args.\n" +
                                "/bounty add <username> <amount> <item>"
                );
                argsAreValid = false;
            }

            if (argsAreValid) {
                Bounty bounty = new Bounty(employer, fugitive, reward, material);

                ItemStack itemStack = new ItemStack(bounty.getMaterial());
                boolean employerCanPay = employer.getInventory().containsAtLeast(itemStack, bounty.getAmount());

                if (employerCanPay) {
                    this.board.add(bounty);

                    Bukkit.broadcastMessage(
                            ChatColor.YELLOW + employer.getDisplayName()
                                    + ChatColor.WHITE + " added a new bounty : Killing "
                                    + ChatColor.RED + fugitive.getDisplayName()
                                    + ChatColor.WHITE + " for "
                                    + ChatColor.BLUE + bounty.getAmount() + " " + bounty.getMaterial().toString()
                    );

                    employer.getInventory().remove(itemStack);
                    this.consumeItem(bounty.getEmployer(), bounty.getMaterial(), bounty.getAmount());
                    this.refreshGlowing();
                } else {
                    employer.sendMessage(
                            ChatColor.RED + "You should have "
                                    + ChatColor.BLUE + bounty.getAmount() + " " + bounty.getMaterial().toString()
                                    + ChatColor.RED + " in your inventory."
                    );
                }
            }
        }
        return true;
    }

    public boolean onBountyListCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            for (int index = 0; index < this.board.size(); index++) {
                Bounty bounty = this.board.get(index);
                player.sendMessage(
                        ChatColor.WHITE + "Bounty "
                                + ChatColor.YELLOW + "#" + index
                                + ChatColor.WHITE + " : Killing "
                                + ChatColor.RED + bounty.getFugitive().getDisplayName()
                                + ChatColor.WHITE + " for "
                                + ChatColor.BLUE + bounty.getAmount() + " " + bounty.getMaterial().toString()
                                + ChatColor.WHITE + " by "
                                + ChatColor.YELLOW + bounty.getEmployer().getDisplayName()
                );
            }

            if (this.board.size() == 0) {
                player.sendMessage(ChatColor.RED + "No bounties registered on the board.");
            }
        }
        return true;
    }

    public boolean onBountyRmCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (strings.length > 0) {
                int index = Integer.parseInt(strings[1]);

                if (index < this.board.size()) {
                    Bounty bounty = this.board.get(index);

                    if (bounty.getEmployer().equals(player)) {
                        this.board.remove(index);
                        player.sendMessage(
                                ChatColor.GREEN + "Bounty #"
                                        + ChatColor.YELLOW + index
                                        + ChatColor.GREEN + " aborted and refunded."
                        );
                        Bukkit.broadcastMessage(
                                ChatColor.YELLOW + bounty.getEmployer().getDisplayName()
                                        + " cancelled the Bounty #" + index
                        );
                        this.giveItem(bounty.getEmployer(), bounty.getMaterial(), bounty.getAmount());
                        this.refreshGlowing();
                    } else {
                        player.sendMessage(
                                ChatColor.RED + "Bounty #"
                                        + ChatColor.YELLOW + index
                                        + ChatColor.RED + " wasn't created by you."
                        );
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "You have to specify a Bounty number.");
            }
        }
        return true;
    }

    private void consumeItem(Player player, Material material, Integer amount) {
        HashMap<Integer, ? extends ItemStack> slots = player.getInventory().all(material);

        for (ItemStack itemStack : slots.values()) {
            if (itemStack.getAmount() < amount) {
                amount -= itemStack.getAmount();
                player.getInventory().remove(itemStack);
            } else {
                itemStack.setAmount(
                        itemStack.getAmount() - amount
                );
            }
        }
    }

    private void giveItem(Player player, Material material, Integer amount) {
        ItemStack itemStack = new ItemStack(material);
        itemStack.setAmount(amount);
        player.getInventory().addItem(itemStack);
    }

    private void refreshGlowing() {
        Collection<? extends Player> players = this.plugin.getServer().getOnlinePlayers();

        for (Player player : players) {
            player.removePotionEffect(PotionEffectType.GLOWING);
        }

        for (Bounty bounty : this.board) {
            PotionEffect potionEffect = new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1);
            bounty.getFugitive().addPotionEffect(potionEffect);
        }
    }
}
