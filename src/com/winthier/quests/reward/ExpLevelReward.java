package com.winthier.quests.reward;

import com.winthier.quests.quest.Quest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ExpLevelReward extends AbstractReward {
        private int level;
        private boolean force;

        public ExpLevelReward(Quest quest) {
                super(quest);
        }

        @Override
        public void load(ConfigurationSection config) {
                super.load(config);
                level = config.getInt("Level", 0);
                force = config.getBoolean("Force", false);
        }

        @Override
        public void give(Player player) {
                if (force) {
                        player.setLevel(level);
                } else {
                        player.setLevel(Math.max(level, player.getLevel()));
                }
        }

        @Override
        public String getDescription() {
                return "XP level " + level;
        }
}
