package com.winthier.quests.reward;

import com.winthier.quests.quest.Quest;
import org.bukkit.configuration.ConfigurationSection;

public abstract class AbstractReward implements Reward {
        protected final Quest quest;

        public AbstractReward(Quest quest) {
                this.quest = quest;
        }

        @Override
        public void load(ConfigurationSection config) {}
}
