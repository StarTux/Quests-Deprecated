package com.winthier.quests.reward;

import com.winthier.quests.quest.Quest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ExpReward extends AbstractReward {
        private int amount;

        public ExpReward(Quest quest) {
                super(quest);
        }

        @Override
        public void load(ConfigurationSection config) {
                super.load(config);
                amount = config.getInt("Amount", 0);
        }

        @Override
        public void give(Player player) {
                player.giveExp(amount);
        }

        @Override
        public String getDescription() {
                return "" + amount + "xp";
        }
}
