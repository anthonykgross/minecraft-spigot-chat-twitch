package fr.anthonykgross.TwitchChatPlugin;

import fr.anthonykgross.TwitchChatPlugin.Listerners.PlayerEventListener;
import org.bukkit.plugin.java.JavaPlugin;

public class TwitchChatPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("onEnable is called!");
        this.getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);
        this.getCommand("lookup").setExecutor(new LookupCommand(
                this.getServer()
        ));
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }

}