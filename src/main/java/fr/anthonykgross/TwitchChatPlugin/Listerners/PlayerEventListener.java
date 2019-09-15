package fr.anthonykgross.TwitchChatPlugin.Listerners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerEventListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent p) {
        PotionEffect potionEffect = new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1);
        potionEffect.apply((LivingEntity)p.getPlayer());
        // Bukkit.broadcastMessage("Hey "+p.getDisplayName()+" ! Bienvenue sur mon serveur moddé. Abonne-toi si tu kiffes ce tuto ! ");
        // Bukkit.broadcastMessage("Like, partage etc .. et fais moi signe si tu en veux davantage en live ! ");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = (Player)e.getEntity();
        Bukkit.broadcastMessage(e.getDeathMessage());

        ItemStack diamonds = new ItemStack(Material.DIAMOND);
        diamonds.setAmount(10);

        Location location = player.getLocation();
        location.getWorld().dropItem(location, diamonds);
    }
}
