package com.technovision.jackpot.messages;

import com.cryptomorin.xseries.XMaterial;
import com.technovision.jackpot.Jackpot;
import com.technovision.jackpot.system.JackpotManager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static com.technovision.jackpot.Jackpot.*;

public class MessageHandler {

    public static List<String> parseResults(String prize, String winner) {
        List<String> msg = Jackpot.PLUGIN.getConfig().getStringList("messages.jackpot-results");
        List<String> parsedMsg = new ArrayList<String>();
        for (String line : msg) {
            line = line.replace("{prize}", prize);
            line = line.replace("{winner}", winner);
            line = line.replace("&", "§");
            parsedMsg.add(line);
        }
        return parsedMsg;
    }

    public static List<String> parseInfo(String totalValue, String tax, long totalTickets, long playerTickets, int playerPercent) {
        List<String> msg = Jackpot.PLUGIN.getConfig().getStringList("messages.jackpot-info");
        List<String> parsedMsg = new ArrayList<String>();
        for (String line : msg) {
            line = line.replace("{total-value}", totalValue);
            line = line.replace("{tax}", tax);
            line = line.replace("{total-tickets}", FORMATTER.format(totalTickets));
            line = line.replace("{player-tickets}", FORMATTER.format(playerTickets));
            line = line.replace("{player-percent}", String.valueOf(playerPercent));
            line = line.replace("{timer}", TIMER.getTime());
            line = line.replace("&", "§");
            parsedMsg.add(line);
        }
        return parsedMsg;
    }

    public static List<String> parseWarning() {
        List<String> msg = Jackpot.PLUGIN.getConfig().getStringList("messages.jackpot-warning");
        List<String> parsedMsg = new ArrayList<String>();
        String money = FORMATTER.format(JackpotManager.MONEY);
        for (String line : msg) {
            line = line.replace("{total-value}", money);
            line = line.replace("{timer}", TIMER.getShortTime());
            line = line.replace("&", "§");
            parsedMsg.add(line);
        }
        return parsedMsg;
    }

    public static String parseBuyMessage(String section, long amt) {
        String msg = Jackpot.PLUGIN.getConfig().getString("messages." + section);
        msg = msg.replace("{amount}", String.valueOf(amt));
        msg = msg.replace("{price}", FORMATTER.format(PLUGIN.getConfig().getInt("jackpot.ticket-price") * amt));
        msg = msg.replace("&", "§");
        return msg;
    }

    public static String parseGUIMessage(String section, long amt, long price) {
        String msg = Jackpot.PLUGIN.getConfig().getString("confirm-gui." + section);
        msg = msg.replace("{amount}", FORMATTER.format(amt));
        msg = msg.replace("{price}", FORMATTER.format(price));
        msg = msg.replace("&", "§");
        return msg;
    }

    public static ItemStack parseItem(String button, String amt, String price) {
        String material = PLUGIN.getConfig().getString("confirm-gui." + button + ".material");
        String name = PLUGIN.getConfig().getString("confirm-gui." + button + ".name");
        List<String> lore = PLUGIN.getConfig().getStringList("confirm-gui." + button + ".lore");

        name = name.replace("&","§" );
        name = name.replace("{amount}", amt);
        name = name.replace("{price}", price);

        List<String> parsedLore = new ArrayList<String>();
        for (String line : lore) {
            line = line.replace("{amount}", amt);
            line = line.replace("{price}", price);
            line = line.replace("&", "§");
            parsedLore.add(line);
        }

        try {
            ItemStack item = XMaterial.valueOf(material).parseItem();
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            meta.setLore(parsedLore);
            item.setItemMeta(meta);
            return item;
        } catch (IllegalArgumentException ignored) { }
        return XMaterial.AIR.parseItem();
    }

    public static int getJackpotValue(String section) {
        return PLUGIN.getConfig().getInt("jackpot." + section);
    }

    public static double getJackpotDouble(String section) {
        return PLUGIN.getConfig().getDouble("jackpot." + section);
    }

    public static boolean getJackpotBoolean(String section) { return PLUGIN.getConfig().getBoolean("jackpot." + section); }
}
