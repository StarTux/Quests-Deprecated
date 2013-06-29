package com.winthier.quests.reward;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public interface Reward {
        public void load(ConfigurationSection config);
        public void give(Player player);
        public String getDescription();
}
