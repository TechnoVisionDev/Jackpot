package com.technovision.jackpot;

import com.technovision.jackpot.gui.JackpotEvents;
import com.technovision.jackpot.system.JackpotManager;
import com.technovision.jackpot.system.Timer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;
import java.util.logging.Logger;

public class Jackpot extends JavaPlugin implements CommandExecutor {

    private static final Logger LOG = Logger.getLogger("Minecraft");
    public static JavaPlugin PLUGIN;
    public static Timer TIMER;
    public static Economy ECON;
    public static DecimalFormat FORMATTER;
    public static JackpotManager JACKPOT;

    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            LOG.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        PLUGIN = this;
        loadConfig();
        FORMATTER = new DecimalFormat("#,###");
        JACKPOT = new JackpotManager();
        TIMER = new Timer();
        TIMER.runTaskTimer(this, 0L, 20L);
        getServer().getPluginManager().registerEvents(new JackpotEvents(), this);
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveConfig();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        ECON = rsp.getProvider();
        return ECON != null;
    }
}
