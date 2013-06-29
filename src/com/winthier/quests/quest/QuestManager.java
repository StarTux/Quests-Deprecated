package com.winthier.quests.quest;

import com.winthier.quests.QuestsPlugin;
import com.winthier.quests.util.Util;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class QuestManager {
        private final QuestsPlugin plugin;
        private final Map<String, CategoryInfo> categories = new LinkedHashMap<String, CategoryInfo>();
        private final Map<String, List<Quest>> quests = new LinkedHashMap<String, List<Quest>>();
        
        public QuestManager(QuestsPlugin plugin) {
                this.plugin = plugin;
        }

        public void onEnable() {
                loadFromFile();
        }

        public void onDisable() {
        }

        // Getters

        /**
         * Never returns null
         */
        public List<Quest> getQuestList(String category) {
                List<Quest> result = quests.get(category);
                if (result == null) {
                        result = new ArrayList<Quest>();
                        quests.put(category, result);
                }
                return result;
        }

        public Quest getQuest(String category, String questName) {
                return null;
        }

        public Collection<String> getCategories() {
                return categories.keySet();
        }

        public CategoryInfo getCategoryInfo(String category) {
                return categories.get(category);
        }

        // File Loading routines

        public void loadFromFile() {
                // Open categories.yml and load categories
                File dir = new File(plugin.getDataFolder(), "quests");
                if (!dir.isDirectory()) {
                        plugin.logWarning("Quests directory does not exist: " + dir.getPath());
                        return;
                }
                String filenames[] = dir.list();
                Arrays.sort(filenames);
                for (String fn : filenames) {
                        File file = new File(dir, fn);
                        // read file
                        YamlConfiguration config;
                        if (!file.isFile() || !file.canRead()) {
                                plugin.logWarning("Can't read quest file: " + file.getPath());
                                continue;
                        }
                        config = YamlConfiguration.loadConfiguration(file);
                        // load CategoryInfo
                        CategoryInfo info = new CategoryInfo();
                        info.load(config);
                        categories.put(info.getName(), info);
                        // load quests
                        ConfigurationSection section = config.createSection("questmap");
                        int itemNumber = 0;
                        for (Map<?, ?> map : config.getMapList("quests")) {
                                itemNumber++;
                                // no public function to turn a list item into a configuration
                                if (map.get("Name") == null) {
                                        plugin.logWarning(file.getPath() + ": quest #" + itemNumber + " lacks key \"Name\"");
                                } else {
                                        loadQuest(info.getName(), section.createSection(map.get("Name").toString(), map));
                                }
                        }
                }
        }

        public void loadQuest(String category, ConfigurationSection section) {
                String questName = section.getString("Name");
                // System.out.println("loading quest " + questName);
                String questType = section.getString("Type", "null");
                Quest quest = new Quest(plugin);
                quest.setCategoryAndName(category, questName);
                try {
                        quest.load(section);
                } catch (Throwable t) {
                        t.printStackTrace();
                }
                getQuestList(category).add(quest);
                quest.enable();
        }
}
