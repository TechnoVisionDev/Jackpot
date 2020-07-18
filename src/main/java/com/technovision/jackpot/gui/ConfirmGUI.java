package com.technovision.jackpot.gui;

import com.technovision.jackpot.Jackpot;
import com.technovision.jackpot.messages.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ConfirmGUI implements InventoryHolder {

    private final Inventory inv;
    private final long tickets;
    private final long amt;

    public ConfirmGUI(long tickets, long amt) {
        this.tickets = tickets;
        this.amt = amt;
        String title = MessageHandler.parseGUIMessage("title", tickets, amt);
        inv = Bukkit.createInventory(this, 9, title);
        init(Jackpot.FORMATTER.format(tickets), Jackpot.FORMATTER.format(amt));
    }

    private void init(String tickets, String amt) {
        ItemStack confirm = MessageHandler.parseItem("confirm-button", tickets, amt);
        ItemStack cancel = MessageHandler.parseItem("cancel-button", tickets, amt);
        ItemStack center = MessageHandler.parseItem("center-button", tickets, amt);
        for (int i = 0; i < 9; i++) {
            if (i < 4) {
                inv.setItem(inv.firstEmpty(), confirm);
            } else if (i == 4) {
                inv.setItem(inv.firstEmpty(), center);
            } else {
                inv.setItem(inv.firstEmpty(), cancel);
            }
        }
    }

    public long getTickets() { return tickets; }

    public long getPrice() { return amt; }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
