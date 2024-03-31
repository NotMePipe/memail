package not.mepipe.memail;

import not.mepipe.memail.commands.MailCommand;
import not.mepipe.memail.listeners.PlayerJoinListener;
import not.mepipe.memail.managers.MailManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;

        registerListeners();
        registerManagers();
        registerCommands();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
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
