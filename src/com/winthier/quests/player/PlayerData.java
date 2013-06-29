package com.winthier.quests.player;

import com.winthier.quests.QuestsPlugin;
import com.winthier.quests.quest.Quest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public abstract class PlayerData {
        public final QuestsPlugin plugin;
        public final String name;
        protected final ConfigurationSection questData;
        // the list of completed quests
        protected final Map<String, Set<String>> completedQuests = new HashMap<String, Set<String>>();
        // the list of unlocked categories
        protected final Set<String> unlockedCategories = new HashSet<String>();
        // cache of the current category and quest
        protected String currentCategory;
        protected Quest currentQuest;
        //
        public boolean seen = false;

        public PlayerData(QuestsPlugin plugin, String name, ConfigurationSection questData) {
                this.plugin = plugin;
                this.name = name;
                this.questData = questData;
        }

        // Getters and setters - constants

        public String getName() {
                return name;
        }

        public Player getPlayer() {
                return plugin.getServer().getPlayerExact(name);
        }

        // Getters and setters

        public String getCurrentCategory() {
                return currentCategory;
        }

        public void setCurrentCategory(String category) {
                currentCategory = category;
        }

        public Quest getCurrentQuest() {
                return currentQuest;
        }

        public Set<String> getUnlockedCategories() {
                return unlockedCategories;
        }

        public void unlockCategory(String category) {
                unlockedCategories.add(category);
        }

        /**
         * Update and return
         */
        public Quest findCurrentQuest() {
                updateCurrentQuest();
                return currentQuest;
        }

        /**
         * Attempt to find the next quest within the current
         * category.  This will fail if the current quest isn't
         * completed.
         * @return true if the current quest was updated, false otherwise
         */
        public boolean updateCurrentQuest() {
                if (currentCategory == null) currentCategory = plugin.defaultCategory;
                Quest quest = findNextQuest(currentCategory);
                if (quest == currentQuest) return false;
                setCurrentQuest(quest);
                return true;
        }

        /**
         * Find the next quest in the current category.
         */
        public Quest findNextQuest(String category) {
                List<Quest> l = plugin.questManager.getQuestList(category);
                for (Quest q : l) {
                        if (!isQuestCompleted(category, q.getName())) return q;
                }
                return null;
        }

        /**
         * Helper function to set the current quest, making sure
         * that the relevant quests are aware of this change.
         */
        public void setCurrentQuest(Quest quest) {
                if (currentQuest == quest) return;
                if (currentQuest != null) currentQuest.removePlayer(this);
                currentQuest = quest;
                if (currentQuest != null) currentQuest.addPlayer(this);
                seen = false;
        }

        // Data management procedures

        /**
         * Never return null
         */
        private ConfigurationSection getCategoryData(String category) {
                ConfigurationSection questsSection = questData.getConfigurationSection("data");
                if (questsSection == null) questsSection = questData.createSection("data");
                ConfigurationSection result = questsSection.getConfigurationSection(category);
                if (result == null) result = questsSection.createSection(category);
                return result;
        }

        public void setQuestData(String category, String quest, ConfigurationSection data) {
                getCategoryData(category).set(quest, data);
        }

        public ConfigurationSection getQuestData(String category, String quest) {
                ConfigurationSection categoryData = getCategoryData(category);
                ConfigurationSection result = categoryData.getConfigurationSection(quest);
                if (result == null) result = categoryData.createSection(quest);
                return result;
        }

        public ConfigurationSection getQuestData(Quest quest) {
                return getQuestData(quest.getCategory(), quest.getName());
        }

        public void resetQuestData(String category, String quest) {
                ConfigurationSection categoryData = getCategoryData(category);
                categoryData.set(quest, null);
                setQuestCompleted(category, quest, false);
        }

        public void resetQuestData(Quest quest) {
                resetQuestData(quest.getCategory(), quest.getName());
        }

        public void setQuestCompleted(Quest quest, boolean completed) {
                setQuestCompleted(quest.getCategory(), quest.getName(), completed);
        }

        public void setQuestCompleted(String category, String questName, boolean completed) {
                Set<String> l = completedQuests.get(category);
                if (completed) {
                        if (l == null) {
                                l = new HashSet<String>();
                                completedQuests.put(category, l);
                        }
                        l.add(questName);
                } else {
                        if (l == null) return;
                        l.remove(questName);
                }
        }

        private boolean isQuestCompleted(String category, String questName) {
                Set<String> l = completedQuests.get(category);
                if (l == null) return false;
                return l.contains(questName);
        }

        public boolean isQuestCompleted(Quest quest) {
                return isQuestCompleted(quest.getCategory(), quest.getName());
        }

        public void clear() {
                onClear();
                if (currentQuest != null) {
                        currentQuest.removePlayer(this);
                        currentQuest = null;
                }
        }

        // Hooks

        /** Called when this instance is loaded into memory and should be initialized */
        public abstract void onLoad();
        /** Called when this instance is about to be save to the data store */
        public abstract void onSave();
        /** Called when this instance is about to be removed from memory */
        public abstract void onClear();
}
