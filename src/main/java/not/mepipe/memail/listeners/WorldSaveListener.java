package not.mepipe.memail.listeners;

import net.kyori.adventure.text.format.TextColor;
import not.mepipe.memail.Main;
import not.mepipe.memail.utils.ChatComponent;
import not.mepipe.memail.utils.IndexedHashmap;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

public class WorldSaveListener implements Listener {

    @EventHandler
    public void onWorldSave(WorldSaveEvent e) {
        IndexedHashmap<String, TextColor> message = new IndexedHashmap<>();
        message.add("Game data saved", TextColor.color(170, 170, 170));

        if(e.getWorld().getName().equals("world_the_end")) {
            Main.getPlugin().saveData();
            Bukkit.broadcast(ChatComponent.build(message));
        }
    }
}
