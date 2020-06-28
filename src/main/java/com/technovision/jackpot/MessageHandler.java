package com.technovision.jackpot;

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

    public static List<String> parseInfo(String totalValue, String tax, long totalTickets, int playerTickets, int playerPercent) {
        List<String> msg = Jackpot.PLUGIN.getConfig().getStringList("messages.jackpot-info");
        List<String> parsedMsg = new ArrayList<String>();
        for (String line : msg) {
            line = line.replace("{total-value}", totalValue);
            line = line.replace("{tax}", tax);
            line = line.replace("{total-tickets}", String.valueOf(totalTickets));
            line = line.replace("{player-tickets}", String.valueOf(playerTickets));
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

    public static String parseBuyMessage(String section, int amt) {
        String msg = Jackpot.PLUGIN.getConfig().getString("messages." + section);
        msg = msg.replace("{amount}", String.valueOf(amt));
        msg = msg.replace("{price}", FORMATTER.format(PLUGIN.getConfig().getInt("jackpot.ticket-price") * amt));
        msg = msg.replace("&", "§");
        return msg;
    }

    public static int getJackpotValue(String section) {
        return PLUGIN.getConfig().getInt("jackpot." + section);
    }

    public static double getJackpotDouble(String section) {
        return PLUGIN.getConfig().getDouble("jackpot." + section);
    }
}
