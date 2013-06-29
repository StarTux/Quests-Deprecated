package com.winthier.quests.effect;

import com.winthier.adviceanimals.event.AdviceAnimalEvent;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Configuration:
 * animal:
 *   Type: AdviceAnimal
 *   AnimalName: Welcome Cat
 *   Messages:
 *   - Howdy!
 *   - Talk to someone else, meow meow.
 */
public class AdviceAnimalEffect extends AbstractEffect {
        private String animalName;
        private final List<String> messages = new ArrayList<String>();

        public AdviceAnimalEffect() {}

        @Override
        public void load(ConfigurationSection config) {
                super.load(config);
                animalName = config.getString("AnimalName");
                for (String line : config.getStringList("Messages")) {
                        messages.add(ChatColor.translateAlternateColorCodes('&', line));
                }
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        public void onAdviceAnimal(AdviceAnimalEvent event) {
                if (messages.isEmpty()) return;
                if (!animalName.equals(event.getAdviceAnimal().getName())) return;
                Player player = event.getPlayer();
                if (!checkConstraints(player)) return;
                int i = quest.getPlayerQuestData(player).getInt(key, 0);
                if (i >= messages.size()) i = 0;
                event.setMessage(messages.get(i));
                quest.getPlayerQuestData(player).set(key, i + 1);
        }
}
