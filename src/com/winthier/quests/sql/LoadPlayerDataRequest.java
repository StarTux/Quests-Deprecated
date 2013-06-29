package com.winthier.quests.sql;

import com.winthier.libsql.PluginSQLRequest;
import com.winthier.quests.QuestsPlugin;
import com.winthier.quests.player.PlayerData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class LoadPlayerDataRequest extends PluginSQLRequest {
        private PlayerData playerData;
        private PreparedStatement statement;

        public LoadPlayerDataRequest(QuestsPlugin plugin, PlayerData playerData) {
                super(plugin);
                this.playerData = playerData;
        }

        @Override
        public void execute(Connection connection) throws SQLException {
                statement = connection.prepareStatement(
                        "SELECT category,quest,completed,data" +
                        " FROM quest_data" +
                        " WHERE player = ?");
                statement.setString(1, playerData.getName());
                statement.executeUpdate();
                ResultSet result = statement.getResultSet();
                callback(result);
        }

        @Override
        public void run() {
                try {
                        while (result.next()) {
                                String category = result.getString("category");
                                String quest = result.getString("quest");
                                boolean completed = result.getBoolean("completed");
                                String data = result.getString("data");
                                YamlConfiguration config = new YamlConfiguration();
                                try {
                                        config.loadFromString(data);
                                } catch (InvalidConfigurationException ice) {
                                        plugin.getLogger().warning("Error while loading quest data " + category + "." + quest + " for player " + playerData.getName());
                                        plugin.getLogger().warning(data);
                                        ice.printStackTrace();
                                }
                                playerData.setQuestData(category, quest, config);
                                playerData.setQuestCompleted(category, quest, completed);
                        }
                } catch (SQLException sqle) {
                        sqle.printStackTrace();
                } finally {
                        try { statement.close(); } catch (SQLException sqle) {}
                }
        }
}
