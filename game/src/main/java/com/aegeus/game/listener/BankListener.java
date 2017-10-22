package com.aegeus.game.listener;

import com.aegeus.game.Aegeus;
import com.aegeus.game.item.Bank;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Team Tower Defense on 9/26/2017.
 */
public class BankListener implements Listener {

    private Aegeus plugin;

    public BankListener(Aegeus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {

    }

    @EventHandler
    public void onBankOpen(PlayerInteractEvent e) throws Exception {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.STONE) {
            Bank b = new Bank(Aegeus.getInstance().getPlayer(e.getPlayer()), 54);
            e.getPlayer().openInventory(b.getInventory(1));
        }
    }
}