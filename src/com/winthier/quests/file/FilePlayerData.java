package com.winthier.quests.file;

import com.winthier.quests.QuestsPlugin;
import com.winthier.quests.player.PlayerData;
import com.winthier.quests.quest.Quest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;

public class FilePlayerData extends PlayerData {
        public FilePlayerData(QuestsPlugin plugin, String name, ConfigurationSection questData) {
                super(plugin, name, questData);
        }

        @Override
        public void onLoad() {
                {
                        unlockedCategories.clear();
                        unlockedCategories.add(plugin.defaultCategory);
                        unlockedCategories.addAll(questData.getStringList("unlocked"));
                } {
                        completedQuests.clear();
                        ConfigurationSection completedSection = questData.getConfigurationSection("completed");
                        if (completedSection != null) {
                                for (String key : completedSection.getKeys(false)) {
                                        completedQuests.put(key, new HashSet<String>(completedSection.getStringList(key)));
                                }
                        }
                }
                currentCategory = questData.getString("CurrentCategory", plugin.defaultCategory);
                updateCurrentQuest();
        }

        @Override
        public void onSave() {
                {
                        questData.set("unlocked", new ArrayList<String>(unlockedCategories));
                } {
                        Map<String, List<String>> completed = new HashMap<String, List<String>>();
                        for (String key : completedQuests.keySet()) {
                                completed.put(key, new ArrayList<String>(completedQuests.get(key)));
                        }
                        questData.set("completed", completed);
                }
                if (currentQuest != null) currentQuest.savePlayer(this);
                questData.set("CurrentCategory", currentCategory);
        }

        @Override
        public void onClear() {
        }
}
