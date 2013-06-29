package com.winthier.quests.reward;

import com.winthier.quests.player.PlayerData;
import com.winthier.quests.quest.Quest;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * This reward type unlocks a new category
 */
public class CategoryReward extends AbstractReward {
        private List<String> unlock;
        private String current;

        public CategoryReward(Quest quest) {
                super(quest);
        }

        @Override
        public void load(ConfigurationSection config) {
                super.load(config);
                unlock = config.getStringList("Unlock");
                current = config.getString("Current", null);
                if (current != null && !unlock.contains(current)) unlock.add(current);
        }

        @Override
        public void give(Player player) {
                PlayerData data = quest.plugin.playerManager.getPlayerData(player);
                for (String category : unlock) {
                        data.unlockCategory(category);
                }
                if (current != null) {
                        data.setCurrentCategory(current);
                }
        }

        @Override
        public String getDescription() {
                if (unlock.isEmpty()) return null;
                StringBuilder sb = new StringBuilder("Quest: ");
                for (String category : unlock) {
                        sb.append(" ").append(category);
                }
                return sb.toString();
        }
}
