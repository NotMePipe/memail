package not.mepipe.memail;

import not.mepipe.memail.commands.MailCommand;
import not.mepipe.memail.listeners.PlayerJoinListener;
import not.mepipe.memail.listeners.WorldSaveListener;
import not.mepipe.memail.managers.MailManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    FileConfiguration config = getConfig();

    private static Main plugin;

    public static final String name = "MeMail";

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
        getServer().getPluginManager().registerEvents(new WorldSaveListener(), this);
    }

    private void registerManagers() {
        MailManager.getInstance().loadMailFile();
    }

    public void saveData() {
        MailManager.getInstance().saveMailFile();
    }

    private void registerCommands() {
        new MailCommand();
    }

    public static Main getPlugin() {
        return plugin;
    }

    @Override
    public void onDisable() {
        saveData();
    }
}
