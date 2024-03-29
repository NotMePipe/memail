package not.mepipe.memail;

import not.mepipe.memail.listeners.PlayerJoinListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    FileConfiguration config = getConfig();

    private static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;
        config.options().copyDefaults(true);
        saveDefaultConfig();

        registerListeners();
        registerManagers();
        registerCommands();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    private void registerManagers() {

    }

    private void registerCommands() {

    }

    public static Main getPlugin() {
        return plugin;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
