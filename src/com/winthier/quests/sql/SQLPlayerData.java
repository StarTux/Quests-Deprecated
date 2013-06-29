package com.winthier.quests.sql;

import com.winthier.quests.QuestsPlugin;
import com.winthier.quests.player.PlayerData;
import org.bukkit.configuration.ConfigurationSection;

/**
 * PlayerData based on data stored in an SQL data source.
 */
public class SQLPlayerData extends PlayerData {
        public SQLPlayerData(QuestsPlugin plugin, String name, ConfigurationSection questData) {
                super(plugin, name, questData);
        }

        @Override
        public void onLoad() {
                plugin.sqlManager.loadPlayerData(this);
        }

        @Override
        public void onSave() {
                // Hopefully we can save whenever data change, so
                // this should always be a no-op.
        }

        @Override
        public void onClear() {
                // do nothing
        }
}
