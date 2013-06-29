package com.winthier.quests.player;

import com.winthier.quests.QuestsPlugin;
import com.winthier.quests.file.FilePlayerData;
import com.winthier.quests.sql.SQLPlayerData;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PlayerManager {
        protected final QuestsPlugin plugin;
        private final Map<String, PlayerData> dataMap = new HashMap<String, PlayerData>();
        public final YamlConfiguration playerQuestData = new YamlConfiguration();

        public PlayerManager(QuestsPlugin plugin) {
                this.plugin = plugin;
        }

        public void onEnable() {
                if (!plugin.useSQL) loadFromFile();
        }

        public void onDisable() {
                if (!plugin.useSQL) saveToFile();
        }

        public PlayerData getPlayerData(String name) {
                PlayerData result = dataMap.get(name);
                if (result == null) {
                        ConfigurationSection playerSection = playerQuestData.getConfigurationSection(name);
                        if (playerSection == null) {
                                playerSection = playerQuestData.createSection(name);
                        }
                        if (plugin.useSQL) {
                                result = new SQLPlayerData(plugin, name, playerSection);
                        } else {
                                result = new FilePlayerData(plugin, name, playerSection);
                        }
                        dataMap.put(name, result);
                        result.onLoad();
                }
                return result;
        }

        public PlayerData getPlayerData(Player player) {
                return getPlayerData(player.getName());
        }

        public void clearPlayerData(String name) {
                PlayerData data = dataMap.remove(name);
                if (data != null) data.clear();
        }

        public void clearPlayerData(Player player) {
                clearPlayerData(player.getName());
        }

        public void clear() {
                for (String name : dataMap.keySet()) {
                        clearPlayerData(name);
                }
        }

        public void loadFromFile() {
                try {
                        playerQuestData.load(new File(plugin.getDataFolder(), "players.yml"));
                } catch (FileNotFoundException fnfe) {
                        // ignore
                } catch (IOException ioe) {
                        ioe.printStackTrace();
                } catch (InvalidConfigurationException ice) {
                        ice.printStackTrace();
                }
        }

        public void saveToFile() {
                for (PlayerData playerData : dataMap.values()) {
                        playerData.onSave();
                }
                try {
                        playerQuestData.save(new File(plugin.getDataFolder(), "players.yml"));
                } catch (IOException ioe) {
                        ioe.printStackTrace();
                }
        }
}
