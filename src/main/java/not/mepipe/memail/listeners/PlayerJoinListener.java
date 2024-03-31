package not.mepipe.memail.listeners;

import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.format.TextColor;
import not.mepipe.memail.managers.MailManager;
import not.mepipe.memail.utils.ChatComponent;
import not.mepipe.memail.utils.IndexedHashmap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final MailManager manager = MailManager.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        IndexedHashmap<String, TextColor> message = new IndexedHashmap<>();
        if (manager.getPlayerUnreadMail(e.getPlayer()).isEmpty()) {
            message.add("You have no new mail", TextColor.color(255, 170, 0));
        } else {
            message.add("You have " + manager.getPlayerUnreadMail(e.getPlayer()).size() + " unread new mail", TextColor.color(255, 170, 0));
        }
        e.getPlayer().sendMessage(Identity.nil(), ChatComponent.build(message));
    }

}
