package com.winthier.quests.sql;

import com.winthier.libsql.PluginSQLRequest;
import com.winthier.quests.QuestsPlugin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.configuration.file.YamlConfiguration;

public class UpsertQuestDataRequest extends PluginSQLRequest {
        private String player, category, quest, data;
        private boolean completed;

        public UpsertQuestDataRequest(QuestsPlugin plugin, String player, String category, String quest, boolean completed, String data) {
                super(plugin);
                this.player = player;
                this.category = category;
                this.quest = quest;
                this.completed = completed;
                this.data = data;
        }

        public UpsertQuestDataRequest(QuestsPlugin plugin, String player, String category, String quest, boolean completed, YamlConfiguration data) {
                this(plugin, player, category, quest, completed, data.saveToString());
        }

        public void execute(Connection connection) throws SQLException {
                PreparedStatement statement;
                statement = connection.prepareStatement(
                        "INSERT INTO quest_data" +
                        " (player, category, quest, completed, data)" +
                        " VALUES(?, ?, ?, ?, ?)" +
                        " ON DUPLICATE KEY" +
                        " UPDATE" +
                        " completed = ?," +
                        " data = ?");
                int i = 1;
                statement.setString(i++, player);
                statement.setString(i++, category);
                statement.setString(i++, quest);
                statement.setBoolean(i++, completed);
                statement.setString(i++, data);
                // on duplicate key
                statement.setBoolean(i++, completed);
                statement.setString(i++, data);
                // go
                statement.executeUpdate();
                statement.close();
        }
}
