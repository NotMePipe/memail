package not.mepipe.memail.listeners;

import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.format.TextColor;
import not.mepipe.memail.Main;
import not.mepipe.memail.utils.ChatComponent;
import not.mepipe.memail.utils.IndexedHashmap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final Main plugin = Main.getPlugin();
    FileConfiguration config = plugin.getConfig();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (config.getBoolean("display-unread-mail-on-join")) {
            IndexedHashmap<String, TextColor> message = new IndexedHashmap<>();
            message.add("Welcome to the game :)", TextColor.color(255, 85, 85));
            e.getPlayer().sendMessage(Identity.nil(), ChatComponent.build(message));
        }
    }

}
