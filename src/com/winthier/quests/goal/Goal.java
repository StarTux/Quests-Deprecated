package com.winthier.quests.goal;

import com.winthier.quests.player.PlayerData;
import com.winthier.quests.quest.Quest;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * Goals are small parts of quests that are displayed in the quest
 * information with checkboxes informing the player about their
 * progress.
 */
public interface Goal {
        public void setQuestAndKey(Quest quest, String key);
        /** Return whatever goes in the box. */
        public String getTitle();
        /** Return whatever goes in the box. */
        public String getBoxInfo(Player player);
        /** Return whether the player has completed this goal. */
        public boolean isCompleted(Player player);
        /** Load data from the quests configuration */
        public void load(ConfigurationSection data);
        /** Enable this goal */
        public void enable();
}
