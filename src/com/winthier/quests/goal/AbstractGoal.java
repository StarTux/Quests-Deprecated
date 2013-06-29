package com.winthier.quests.goal;

import com.winthier.quests.constraint.Constraints;
import com.winthier.quests.player.PlayerData;
import com.winthier.quests.quest.Quest;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * A Quota Goal wants the player to get a number up to a certain
 * level.
 */
public abstract class AbstractGoal implements Goal, Listener {
        public Quest quest;
        public String key;
        private String title;
        private final Constraints constraints = new Constraints();

        public void setQuestAndKey(Quest quest, String key) {
                this.quest = quest;
                this.key = key;
        }

        @Override
        public void enable() {
                quest.plugin.getServer().getPluginManager().registerEvents(this, quest.plugin);
        }

        @Override
        public String getTitle() {
                return title;
        }

        @Override
        public void load(ConfigurationSection data) {
                title = data.getString("Title", "Progress");
                constraints.load(data.getConfigurationSection("constraints"));
        }

        public boolean checkConstraints(Player player) {
                return quest.checkConstraints(player) && constraints.isSatisfied(player);
        }

        public void logWarning(String msg) {
                quest.logWarning(getClass().getSimpleName() + ": " + msg);
        }
}
