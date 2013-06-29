package com.winthier.quests.reward;

import com.winthier.quests.quest.Quest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * Set the player's food level
 */
public class FoodReward extends AbstractReward {
        private int foodLevel = 20;

        public FoodReward(Quest quest) {
                super(quest);
        }

        @Override
        public void load(ConfigurationSection config) {
                super.load(config);
                foodLevel = config.getInt("Level", 20);
        }

        @Override
        public void give(Player player) {
                player.setFoodLevel(foodLevel);
        }

        @Override
        public String getDescription() {
                return null;
        }
}
