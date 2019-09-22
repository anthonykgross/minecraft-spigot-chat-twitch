package fr.anthonykgross.TwitchChatPlugin.Listerners;

import fr.anthonykgross.TwitchChatPlugin.App;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerEventListener implements Listener {
    private App app;

    public PlayerEventListener(App app) {
        this.app = app;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent p) {
        this.app.onPlayerJoin(p);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        this.app.onPlayerDeath(e);
    }
}
