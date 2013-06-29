package com.winthier.quests.command;

import com.winthier.quests.QuestsPlugin;
import com.winthier.quests.player.PlayerData;
import com.winthier.quests.quest.Quest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuestCommand implements CommandExecutor {
        protected final QuestsPlugin plugin;

        public QuestCommand(QuestsPlugin plugin) {
                this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
                Player player = null;
                if (sender instanceof Player) player = (Player)sender;
                if (player == null) {
                        if (args.length == 0) {
                                sender.sendMessage("Quest categories");
                                for (String i : plugin.questManager.getCategories()) {
                                        sender.sendMessage("- " + i);
                                }
                        } else if (args.length == 1 && args[0].equals("save")) {
                                plugin.playerManager.saveToFile();
                                sender.sendMessage("Player data saved to file");
                        } else if (args.length == 1 && args[0].equals("reload")) {
                                plugin.playerManager.loadFromFile();
                                plugin.playerManager.clear();
                                for (Player p : plugin.getServer().getOnlinePlayers()) plugin.playerManager.getPlayerData(p);
                                sender.sendMessage("Player data reloaded");
                        } else if (args.length == 2) {
                                String playerName = args[0];
                                String category = args[1];
                                player = plugin.getServer().getPlayer(playerName);
                                if (player == null) {
                                        sender.sendMessage("Player not found: " + playerName);
                                        return true;
                                }
                                sender.sendMessage(String.format("Quests from category %s for player %s.", category, playerName));
                                PlayerData playerData = plugin.playerManager.getPlayerData(player);
                                for (Quest q : plugin.questManager.getQuestList(category)) {
                                        sender.sendMessage(String.format("- %s (%s)", q.getName(), playerData.isQuestCompleted(q) ? "completed" : "not completed"));
                                }
                        } else {
                                sender.sendMessage("Player expected");
                        }
                        return true;
                }
                if (args.length == 1 && args[0].equals("starve")) {
                        player.setFoodLevel(0);
                        player.sendMessage("You are starving.");
                        return true;
                } else if (args.length == 1 && args[0].equals("skip")) {
                        plugin.playerManager.getPlayerData(player).findCurrentQuest().setCompleted(player, true);
                        player.sendMessage("Quest completed");
                        plugin.playerManager.getPlayerData(player).findCurrentQuest().displayQuestInfo(player);
                        return true;
                } else if (args.length == 1 && args[0].equals("back")) {
                        PlayerData data = plugin.playerManager.getPlayerData(player);
                        String category = data.getCurrentCategory();
                        Quest lastQuest = null;
                        for (Quest quest : plugin.questManager.getQuestList(category)) {
                                if (!quest.isCompleted(player)) break;
                                lastQuest = quest;
                        }
                        if (lastQuest == null) {
                                player.sendMessage("Can't go back any further");
                                return true;
                        }
                        data.resetQuestData(lastQuest);
                        player.sendMessage("Reset quest: " + lastQuest.getName());
                        data.findCurrentQuest().displayQuestInfo(player);
                        return true;
                } else if (args.length == 1 && args[0].equals("reset")) {
                        PlayerData data = plugin.playerManager.getPlayerData(player);
                        data.resetQuestData(data.findCurrentQuest());
                        player.sendMessage("Current quest reset");
                        data.findCurrentQuest().displayQuestInfo(player);
                        return true;
                } else if (args.length == 1 && args[0].equals("help")) {
                        plugin.playerManager.getPlayerData(player).findCurrentQuest().displayHelp(player);
                        return true;
                } else if (args.length == 1 && args[0].equals("switch")) {
                        plugin.questMenu.openMenu(player);
                        return true;
                } else if (args.length == 0) {
                        Quest q = plugin.playerManager.getPlayerData(player).findCurrentQuest();
                        if (q == null) {
                                player.sendMessage("I got nothing for you ;_;");
                        } else {
                                q.displayQuestInfo(player);
                        }
                        return true;
                }
                return false;
        }
}
