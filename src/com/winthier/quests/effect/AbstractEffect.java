package com.winthier.quests.effect;

import com.winthier.quests.quest.Quest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class AbstractEffect implements Effect, Listener {
        protected Quest quest;
        protected String key;

        @Override
        public void setQuestAndKey(Quest quest, String key) {
                this.quest = quest;
                this.key = key;
        }

        @Override
        public void enable() {
                quest.plugin.getServer().getPluginManager().registerEvents(this, quest.plugin);
        }

        @Override
        public void load(ConfigurationSection config) {}

        protected boolean checkConstraints(Player player) {
                return quest.checkConstraints(player);
        }

        public void logWarning(String msg) {
                quest.logWarning(getClass().getSimpleName() + ": " + msg);
        }
}
