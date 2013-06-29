package com.winthier.quests.sql;

import com.winthier.libsql.ConnectionManager;
import com.winthier.quests.QuestsPlugin;
import com.winthier.quests.player.PlayerData;
import org.bukkit.configuration.file.YamlConfiguration;

public class SQLManager {
        protected final QuestsPlugin plugin;
        protected ConnectionManager connectionManager;

        public SQLManager(QuestsPlugin plugin) {
                this.plugin = plugin;
        }

        public void onEnable() {
                connectionManager = new ConnectionManager(plugin, plugin.getConfig().getConfigurationSection("sql"));
                connectionManager.start();
                connectionManager.queueRequest(new CreateTableRequest(plugin));
        }

        public void onDisable() {
                connectionManager.stop();
        }

        public void updateQuestData(String player, String category, String quest, boolean completed, String data) {
                UpsertQuestDataRequest request = new UpsertQuestDataRequest(plugin, player, category, quest, completed, data);
                connectionManager.queueRequest(request);
        }

        public void updateQuestData(String player, String category, String quest, boolean completed, YamlConfiguration data) {
                UpsertQuestDataRequest request = new UpsertQuestDataRequest(plugin, player, category, quest, completed, data);
                connectionManager.queueRequest(request);
        }

        public void loadPlayerData(PlayerData data) {
                LoadPlayerDataRequest request = new LoadPlayerDataRequest(plugin, data);
                connectionManager.queueRequest(request);
        }
}
