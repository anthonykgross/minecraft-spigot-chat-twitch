package fr.anthonykgross.TwitchChatPlugin.Model;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Bounty {

    private Player employer;
    private Player fugitive;
    private Integer amount = 10;
    private Material material = Material.DIAMOND;


    public Bounty(Player employer, Player fugitive) {
        this.employer = employer;
        this.fugitive = fugitive;
    }

    public Bounty(Player employer, Player fugitive, Integer amount) {
        this(employer, fugitive);
        this.setAmount(amount);
    }

    public Bounty(Player employer, Player fugitive, Integer amount, Material material) {
        this(employer, fugitive, amount);
        this.setMaterial(material);
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        if (amount != null) {
            this.amount = amount;
        }
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Player getEmployer() {
        return employer;
    }

    public Player getFugitive() {
        return fugitive;
    }
}
