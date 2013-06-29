package com.winthier.quests;

import com.winthier.quests.command.QuestCommand;
import com.winthier.quests.gui.QuestMenu;
import com.winthier.quests.listener.PlayerListener;
import com.winthier.quests.player.PlayerManager;
import com.winthier.quests.protocol.Protocol;
import com.winthier.quests.quest.QuestManager;
import com.winthier.quests.sql.SQLManager;
import com.winthier.quests.util.Util;
import com.winthier.quests.vault.Vault;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class QuestsPlugin extends JavaPlugin {
        public SQLManager sqlManager;
        public final QuestManager questManager = new QuestManager(this);
        public final PlayerManager playerManager = new PlayerManager(this);
        public final Vault vault = new Vault(this);
        public final Protocol protocol = new Protocol(this);
        public final PlayerListener playerListener = new PlayerListener(this);
        public final QuestMenu questMenu = new QuestMenu(this);
        public boolean useSQL;
        public long autoUpdateInterval;
        public String defaultCategory;

        @Override
        public void onEnable() {
                reloadConfig();
                getConfig().options().copyDefaults(true);
                saveConfig();
                Util.copyResource(this, "quests/01_Honored.yml", true);
                Util.copyResource(this, "quests/02_Revered.yml", true);
                Util.copyResource(this, "players.yml", false);
                useSQL = getConfig().getBoolean("sql.enable");
                autoUpdateInterval = getConfig().getLong("AutoUpdateInterval");
                defaultCategory = getConfig().getString("DefaultCategory");
                vault.onEnable();
                protocol.onEnable();
                if (useSQL) {
                        sqlManager = new SQLManager(this);
                        sqlManager.onEnable();
                }
                questManager.onEnable();
                playerManager.onEnable();
                questMenu.onEnable();
                getCommand("quest").setExecutor(new QuestCommand(this));
                playerListener.onEnable();
        }

        @Override
        public void onDisable() {
                playerListener.onDisable();
                if (useSQL) {
                        sqlManager.onDisable();
                }
                vault.onDisable();
                protocol.onDisable();
                questManager.onDisable();
                playerManager.onDisable();
        }

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
                return false;
        }

        public void logInfo(String msg) {
                getLogger().info(msg);
        }

        public void logWarning(String msg) {
                getLogger().warning(msg);
        }
}
