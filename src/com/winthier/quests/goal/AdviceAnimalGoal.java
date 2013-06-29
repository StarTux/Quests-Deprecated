package com.winthier.quests.goal;

import com.winthier.adviceanimals.AdviceAnimal;
import com.winthier.adviceanimals.event.AdviceAnimalEvent;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class AdviceAnimalGoal extends QuotaGoal {
        private List<String> animalNames;
        private List<String> messages;

        @Override
        public void load(ConfigurationSection config) {
                super.load(config);
                animalNames = config.getStringList("AnimalNames");
                if (config.isSet("Messages")) {
                        messages = config.getStringList("Messages");
                        // Let's be rude and override the quota
                        quota = messages.size();
                }
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onAdviceAnimal(AdviceAnimalEvent event) {
                Player player = event.getPlayer();
                if (!checkConstraints(player)) return;
                feed(player, event);
        }


        public boolean feed(Player player, AdviceAnimalEvent event) {
                AdviceAnimal animal = event.getAdviceAnimal();
                for (String name : animalNames) {
                        if (name.equals(animal.getName())) {
                                if (messages != null && !messages.isEmpty()) {
                                        String message = messages.get(Math.min(getScore(player), messages.size() - 1));
                                        event.setMessage(message);
                                }
                                addScore(player, 1);
                                return true;
                        }
                }
                return false;
        }
}
