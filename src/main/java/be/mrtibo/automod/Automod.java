package be.mrtibo.automod;

import org.bukkit.plugin.java.JavaPlugin;

public final class Automod extends JavaPlugin {

    static Automod plugin;
    @Override
    public void onEnable() {
        getCommand("automod-reload").setExecutor(new ReloadCommand());
        saveDefaultConfig();
        plugin = this;
        new ChatEvent();
    }

    @Override
    public void onDisable() {
        plugin = null;
    }


    public static Automod getInstance(){
        return plugin;
    }
}
