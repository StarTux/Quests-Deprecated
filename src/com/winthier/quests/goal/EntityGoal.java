package com.winthier.quests.goal;

import com.winthier.quests.util.EntityMatcher;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityGoal extends QuotaGoal {
        private List<EntityMatcher> entities = new ArrayList<EntityMatcher>();

        public EntityGoal() {}

        @Override
        public void load(ConfigurationSection config) {
                super.load(config);
                for (String e : config.getStringList("Entities")) {
                        EntityMatcher matcher = EntityMatcher.fromString(e);
                        if (matcher == null) {
                                quest.logWarning(config.getCurrentPath() + ".Entities." + e + ": invalid entity");
                                continue;
                        }
                        entities.add(matcher);
                }
        }

        public boolean matches(Entity entity) {
                for (EntityMatcher matcher : entities) {
                        if (matcher.matches(entity)) return true;
                }
                return false;
        }

        public boolean feed(Player player, Entity entity) {
                if (matches(entity)) {
                        addScore(player, 1);
                        return true;
                }
                return false;
        }
}
