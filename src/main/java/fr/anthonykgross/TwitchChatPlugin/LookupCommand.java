package fr.anthonykgross.TwitchChatPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LookupCommand implements CommandExecutor {
    private Server server;

    public LookupCommand(Server server) {
        this.server = server;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player victim = this.server.getPlayer(strings[0]);

        if (victim != null) {
            Bukkit.broadcastMessage("Nouvelle tête mise à prix : "+ victim.getDisplayName());
        }

        // Create a new ItemStack (type: diamond)
        // ItemStack diamond = new ItemStack(Material.DIAMOND);

        // Create a new ItemStack (type: brick)
        // ItemStack bricks = new ItemStack(Material.BRICK);

        // Set the amount of the ItemStack
        // bricks.setAmount(20);

        // Give the player our items (comma-seperated list of all ItemStack)
        // player.getInventory().addItem(bricks, diamond);
        return true;
    }
}
