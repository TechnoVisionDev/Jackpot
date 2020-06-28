package com.technovision.jackpot;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

import static com.technovision.jackpot.Jackpot.*;

public class JackpotManager implements CommandExecutor {

    public static List<UUID> JACKPOT;
    public static long MONEY;
    public static long TOTAL_TICKETS;
    public static HashMap<String, Integer> TICKETS;

    public JackpotManager() {
        MONEY = 0;
        TOTAL_TICKETS = 0;
        JACKPOT = new ArrayList<UUID>();
        TICKETS = new HashMap<String, Integer>();
        PLUGIN.getCommand("jackpot").setExecutor(this);
    }

    public void awardWinner() {
        if (!JACKPOT.isEmpty()) {
            double prize = MONEY - (MONEY * MessageHandler.getJackpotDouble("tax-percent"));
            String prizeString = FORMATTER.format(prize);
            Collections.shuffle(JACKPOT);
            Player player = Bukkit.getPlayer(JACKPOT.get(0));
            List<String> msg = MessageHandler.parseResults(prizeString, player.getName());
            for (String line : msg) {
                Bukkit.broadcastMessage(line);
            }
            ECON.depositPlayer(player, prize);
        }
        MONEY = 0;
        TOTAL_TICKETS = 0;
        TICKETS.clear();
        JACKPOT.clear();
    }

    public void enterJackpot(Player player, int amt, int total) {
        for (int i = 0; i < amt; i++) {
            JACKPOT.add(player.getUniqueId());
        }
        ECON.withdrawPlayer(player, total);
        MONEY += total;
        TOTAL_TICKETS += amt;
        if (TICKETS.containsKey(player.getUniqueId().toString())) {
            int oldAmt = TICKETS.get(player.getUniqueId().toString());
            TICKETS.put(player.getUniqueId().toString(), oldAmt + amt);
        } else {
            TICKETS.put(player.getUniqueId().toString(), amt);
        }
        player.sendMessage(MessageHandler.parseBuyMessage("buy-ticket", amt));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("jackpot")) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("buy") || args[0].equalsIgnoreCase("bet") || args[0].equalsIgnoreCase("place")) {
                        int amt = 1;
                        if (args.length >= 2) {
                            try {
                                amt = Integer.parseInt(args[1]);
                            } catch (NumberFormatException e) {
                                player.sendMessage("§c§l(!) §c/jackpot buy <amount>");
                                return true;
                            }
                        }
                        int total = MessageHandler.getJackpotValue("ticket-price") * amt;
                        if (ECON.getBalance(player) >= total) {
                            enterJackpot(player, amt, total);
                        } else {
                            player.sendMessage(MessageHandler.parseBuyMessage("cannot-afford", amt));
                        }
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        if (player.hasPermission("jackpot.reload") || player.isOp()) {
                            PLUGIN.reloadConfig();
                            PLUGIN.saveConfig();
                            Bukkit.getServer().getPluginManager().disablePlugin(PLUGIN);
                            Bukkit.getServer().getPluginManager().enablePlugin(PLUGIN);
                            player.sendMessage("§a§l(!) §aJackpot successfully reloaded the config.");
                        } else {
                            player.sendMessage("§c§l(!) §cYou do not have permission to use that command!");
                        }
                    } else {
                        player.sendMessage("§c§l(!) §c/jackpot buy <amount>");
                    }
                } else {
                    int amount = 0;
                    double percent = 0;
                    if (TICKETS.containsKey(player.getUniqueId().toString())) {
                        amount = TICKETS.get(player.getUniqueId().toString());
                        percent = ((double) amount / TOTAL_TICKETS) * 100;
                    }
                    String tax = String.valueOf((int) (MessageHandler.getJackpotDouble("tax-percent") * 100));
                    List<String> msg = MessageHandler.parseInfo(FORMATTER.format(MONEY), tax, TOTAL_TICKETS, amount, (int) percent);
                    for (String line : msg) {
                        player.sendMessage(line);
                    }
                }
            }
        }
        return true;
    }
}
