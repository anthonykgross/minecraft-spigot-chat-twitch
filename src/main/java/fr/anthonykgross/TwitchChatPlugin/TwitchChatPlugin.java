package fr.anthonykgross.TwitchChatPlugin;

import fr.anthonykgross.TwitchChatPlugin.Command.BountyCommand;
import fr.anthonykgross.TwitchChatPlugin.Listerners.PlayerEventListener;
import org.bukkit.plugin.java.JavaPlugin;

public class TwitchChatPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("onEnable is called!");

        App app = new App(this);
        this.getServer().getPluginManager().registerEvents(new PlayerEventListener(app), this);
        this.getCommand("bounty").setExecutor(new BountyCommand(app));
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }

}