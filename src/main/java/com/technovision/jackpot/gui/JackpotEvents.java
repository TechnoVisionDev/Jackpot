package com.technovision.jackpot.gui;

import com.technovision.jackpot.messages.MessageHandler;
import com.technovision.jackpot.system.JackpotManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class JackpotEvents implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) { return; }
        if (e.getClickedInventory().getHolder() instanceof ConfirmGUI) {
            e.setCancelled(true);
            ConfirmGUI gui = (ConfirmGUI) e.getClickedInventory().getHolder();
            Player player = (Player) e.getWhoClicked();
            int slot = e.getSlot();
            if (slot < 4) {
                JackpotManager.enterJackpot(player, gui.getTickets(), gui.getPrice());
                player.closeInventory();
            }
            else if (slot > 4) {
                player.closeInventory();
                player.sendMessage(MessageHandler.parseGUIMessage("cancel-message", gui.getTickets(), gui.getPrice()));
            }
        }
    }
}
