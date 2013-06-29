package com.winthier.quests.reward;

import com.winthier.quests.quest.Quest;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Rewards extends AbstractReward {
        private final List<Reward> rewards = new ArrayList<Reward>();

        public Rewards(Quest quest) {
                super(quest);
        }

        @Override
        public void load(ConfigurationSection config) {
                if (config == null) return;
                super.load(config);
                for (String key : config.getKeys(false)) {
                        Reward reward = RewardType.fromString(quest, key);
                        if (reward == null) {
                                quest.logWarning(config.getCurrentPath() + "." + key + ": invalid reward");
                                continue;
                        }
                        reward.load(config.getConfigurationSection(key));
                        rewards.add(reward);
                }
        }

        @Override
        public void give(Player player) {
                for (Reward reward : rewards) {
                        reward.give(player);
                }
        }

        @Override
        public String getDescription() {
                if (rewards.isEmpty()) return "";
                StringBuilder sb = new StringBuilder("");
                for (int i = 0; i < rewards.size(); ++i) {
                        Reward reward = rewards.get(i);
                        String description = reward.getDescription();
                        if (description != null) {
                                if (sb.length() > 0) sb.append(", ");
                                sb.append(reward.getDescription());
                        }
                }
                return sb.toString();
        }

        public boolean isEmpty() {
                return rewards.isEmpty();
        }
}
