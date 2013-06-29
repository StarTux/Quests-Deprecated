package com.winthier.quests.effect;

import com.winthier.quests.quest.Quest;
import org.bukkit.configuration.ConfigurationSection;

/**
 * An effect is a side effect of being in a quest. It is only
 * effective for players while they are in the possessive quest.
 */
public interface Effect {
        public void enable();
        public void setQuestAndKey(Quest quest, String key);
        public void load(ConfigurationSection config);
}
