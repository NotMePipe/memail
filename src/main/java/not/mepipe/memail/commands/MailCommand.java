package not.mepipe.memail.commands;

import net.kyori.adventure.text.format.TextColor;
import not.mepipe.memail.Main;
import not.mepipe.memail.managers.MailManager;
import not.mepipe.memail.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MailCommand implements CommandExecutor {

    private final MailManager manager = MailManager.getInstance();

    public MailCommand() {
        Objects.requireNonNull(Main.getPlugin().getCommand("mail")).setExecutor(this);
        Objects.requireNonNull(Main.getPlugin().getCommand("mail")).setTabCompleter(new TabComplete());
    }

    static class TabComplete implements TabCompleter {
        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            ArrayList<String> options = new ArrayList<>();
            if (args.length == 1) {
                options.add("read");
                options.add("send");
                options.add("clear");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("send")) {
                    for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
                        options.add(player.getName());
                    }
                }
            }
            return options;
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        IndexedHashmap<String, TextColor> senderMessage = new IndexedHashmap<>();
        if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("read"))) {
            if (sender instanceof Player) {
                OfflinePlayer p = (OfflinePlayer) sender;
                ArrayList<String> messages = manager.getPlayerMail(p);

                if (!messages.isEmpty()) {
                    for (int i = 0; i < messages.size(); i++) {
                        TextColor color;
                        if (manager.getPlayerMailData(p).getSecond(i)) {
                            color = TextColor.color(85, 255, 85);
                        } else {
                            color = TextColor.color(85, 255, 255);
                        }
                        senderMessage.add(messages.get(i), color);
                        if (i != messages.size() - 1) {
                            senderMessage.add("\n", color);
                        }
                    }

                    manager.markPlayerMailRead(p);
                } else {
                    senderMessage.add("You have no mail", TextColor.color(255, 170, 0));
                }

                sender.sendMessage(ChatComponent.build(senderMessage));
                return true;
            } else {
                senderMessage.add("The server does not have a mailbox!", TextColor.color(255, 85, 85));
                sender.sendMessage(ChatComponent.build(senderMessage));
                return false;
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("send")) {
                senderMessage.add("/mail send <player> [message]\n", TextColor.color(255, 85, 85));
                sender.sendMessage(ChatComponent.build(senderMessage));
                return false;
            } else if (args[0].equalsIgnoreCase("clear")) {
                if (sender instanceof Player) {
                    manager.clearPlayerMail((OfflinePlayer) sender);
                    senderMessage.add("Mailbox emptied!", TextColor.color(255, 255, 85));
                    sender.sendMessage(ChatComponent.build(senderMessage));
                    return false;
                }
            } else if (!args[0].equalsIgnoreCase("read")) {
                senderMessage.add("/mail send <player> [message]\n", TextColor.color(255, 85, 85));
                senderMessage.add("/mail clear\n", TextColor.color(255, 85, 85));
                senderMessage.add("/mail read\n", TextColor.color(255, 85, 85));
                senderMessage.add("/mail", TextColor.color(255, 85, 85));
                sender.sendMessage(ChatComponent.build(senderMessage));
                return false;
            }
        } else if (args.length > 2) {
            if (args[0].equalsIgnoreCase("send")) {
                if (!sender.hasPermission("memail.send")) {
                    return false;
                }

                String senderName;
                if (sender instanceof Player) {
                    senderName = sender.getName();
                } else {
                    senderName = "CONSOLE";
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(Objects.requireNonNull(UUIDFetcher.getUUID(args[1])));

                String message = Helpers.concatenate(args, 2, args.length - 1," ");

                String time = Helpers.getTime(System.currentTimeMillis(), -4); // -4 is EST

                String finalMessage = "[" + time + "] " + senderName + ": " + message;

                manager.addMailToPlayer(target, finalMessage);
            }
        } else {
            senderMessage.add("/mail send <player> [message]\n", TextColor.color(255, 85, 85));
            senderMessage.add("/mail clear\n", TextColor.color(255, 85, 85));
            senderMessage.add("/mail read\n", TextColor.color(255, 85, 85));
            senderMessage.add("/mail", TextColor.color(255, 85, 85));
            sender.sendMessage(ChatComponent.build(senderMessage));
            return false;
        }

        return false;
    }
}
