package fr.anthonykgross.TwitchChatPlugin.Listerners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class PlayerEventListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerBedEnterEvent e) {
        Player p = e.getPlayer();
        Bukkit.broadcastMessage("Hey "+p.getDisplayName()+" ! Bienvenue sur mon serveur moddé. Abonne-toi si tu kiffes ce tuto ! ");
        Bukkit.broadcastMessage("Like, partage etc .. et fais moi signe si tu en veux davantage en live ! ");
    }
}
