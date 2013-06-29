package com.winthier.quests.reward;

import com.winthier.quests.quest.Quest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class MoneyReward extends AbstractReward {
        private double amount;

        public MoneyReward(Quest quest) {
                super(quest);
        }

        @Override
        public void load(ConfigurationSection config) {
                super.load(config);
                amount = config.getDouble("Amount", 0.0);
        }

        @Override
        public void give(Player player) {
                quest.plugin.vault.economy.depositPlayer(player.getName(), amount);
        }

        @Override
        public String getDescription() {
                return quest.plugin.vault.economy.format(amount);
        }
}
