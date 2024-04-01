package not.mepipe.memail.listeners;

import not.mepipe.memail.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

public class WorldSaveListener implements Listener {

    @EventHandler
    public void onWorldSave(WorldSaveEvent e) {
        if(e.getWorld().getName().equals("world_the_end")) {
            Main.getPlugin().saveData();
        }
    }
}
