package com.winthier.quests.sql;

import com.winthier.libsql.PluginSQLRequest;
import com.winthier.quests.QuestsPlugin;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTableRequest extends PluginSQLRequest {
        public CreateTableRequest(QuestsPlugin plugin) {
                super(plugin);
        }

        public void execute(Connection connection) throws SQLException {
                Statement statement;
                statement = connection.createStatement();
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS unlocked_categories (" +
                        " id int(11) NOT NULL AUTO_INCREMENT," +
                        " player VARCHAR(16) NOT NULL," +
                        " category VARCHAR(32) NOT NULL," +
                        " unlocked BIT(1) NOT NULL DEFAULT 0," +
                        " PRIMARY KEY(id)," +
                        " UNIQUE KEY(player, category)" +
                        ") ENGINE=MyISAM");
                statement.close();

                statement = connection.createStatement();
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS quest_data (" +
                        " id int(11) NOT NULL AUTO_INCREMENT," +
                        " player VARCHAR(16) NOT NULL," +
                        " category VARCHAR(32) NOT NULL," +
                        " quest VARCHAR(32) NOT NULL," +
                        " completed BIT(1) NOT NULL," +
                        " PRIMARY KEY(id)," +
                        " UNIQUE KEY(player, category, quest)" +
                        ") ENGINE=MyISAM");
                statement.close();

                statement = connection.createStatement();
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS goal_data (" +
                        " id int(11) NOT NULL AUTO_INCREMENT," +
                        " player VARCHAR(16) NOT NULL," +
                        " category VARCHAR(32) NOT NULL," +
                        " quest VARCHAR(32) NOT NULL," +
                        " key VARCHAR(32) NOT NULL," +
                        " value int(11) DEFAULT 0," +
                        " PRIMARY KEY(id)," +
                        " UNIQUE KEY(player, category, quest, key)" +
                        ") ENGINE=MyISAM");
                statement.close();
        }
}
