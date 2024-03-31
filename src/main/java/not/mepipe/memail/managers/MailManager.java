package not.mepipe.memail.managers;

import not.mepipe.memail.Main;
import not.mepipe.memail.utils.IndexedHashmap;
import not.mepipe.memail.utils.Logger;
import org.bukkit.OfflinePlayer;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class MailManager {

    private static MailManager manager = null;

    private static HashMap<UUID, IndexedHashmap<String, Boolean>> data = new HashMap<>();

    private final Main plugin = Main.getPlugin();

    public static MailManager getInstance() {
        if (manager == null) {
            manager = new MailManager();
        }
        return manager;
    }

    private File getDataFile() {
        File file = new File(plugin.getDataFolder(), "mail.dat");
        if (!file.exists()) {
            Logger.send(Logger.MessageType.INFO, "mail.dat file not found. Attempting to create...");
            try {
                if (file.createNewFile()) {
                    Logger.send(Logger.MessageType.GOOD, "mail.dat file created successfully");
                }
            } catch (IOException e) {
                Logger.send(Logger.MessageType.BAD, "mail.dat file creation failed");
                Logger.send(e);
            }
        } else {
            Logger.send(Logger.MessageType.GOOD, "mail.dat file found");
        }

        return file;
    }

    public void saveMailFile() {
        File file = getDataFile();

        try {
            ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(Files.newOutputStream(file.toPath())));

            output.writeObject(data);
            output.flush();
            output.close();
            Logger.send(Logger.MessageType.GOOD, "Data saved successfully");
        } catch(IOException e) {
            Logger.send(Logger.MessageType.BAD, "Data saving error");
            Logger.send(e);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadMailFile() {
        File file = getDataFile();

        try {
            ObjectInputStream input = new ObjectInputStream(new GZIPInputStream(Files.newInputStream(file.toPath())));
            Object readObject = input.readObject();
            data = (HashMap<UUID, IndexedHashmap<String, Boolean>>) readObject;

            input.close();
            Logger.send(Logger.MessageType.GOOD, "Data loaded successfully");
        } catch (EOFException ignored) {
        } catch (Throwable e) {
            Logger.send(Logger.MessageType.BAD, "Data loading error");
            Logger.send(e);
        }
    }

    public void addMailToPlayer(OfflinePlayer player, String message) {
        IndexedHashmap<String, Boolean> map = getPlayerMailData(player);
        map.add(message, true);
        data.put(player.getUniqueId(), map);
    }

    public void clearPlayerMail(OfflinePlayer player) {
        if (data.get(player.getUniqueId()) != null) {
            data.remove(player.getUniqueId());
        }
    }

    public IndexedHashmap<String, Boolean> getPlayerMailData(OfflinePlayer player) {
        if (data.get(player.getUniqueId()) != null) {
            return data.get(player.getUniqueId());
        } else {
            return new IndexedHashmap<>();
        }
    }

    public ArrayList<String> getPlayerMail(OfflinePlayer player) {
        IndexedHashmap<String, Boolean> map = getPlayerMailData(player);
        ArrayList<String> array = new ArrayList<>();
        for (int i = 0; i < map.size(); i++) {
            array.add(map.getFirst(i));
        }
        return array;
    }

    public ArrayList<String> getPlayerUnreadMail(OfflinePlayer player) {
        IndexedHashmap<String, Boolean> map = getPlayerMailData(player);
        ArrayList<String> array = new ArrayList<>();
        for (int i = 0; i < map.size(); i++) {
            if (map.getSecond(i)) {
                array.add(map.getFirst(i));
            }
        }
        return array;
    }

    public void markPlayerMailRead(OfflinePlayer player) {
        IndexedHashmap<String, Boolean> map = getPlayerMailData(player);
        for (int i = 0; i < map.size(); i++) {
            if (map.getSecond(i)) {
                map.setSecond(i, false);
            }
        }
    }

}
