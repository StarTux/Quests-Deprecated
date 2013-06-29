package com.winthier.quests.constraint;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * This Constraint checks if the Player is in one of some
 * specified worlds.
 *
 * Configuration:
 * world:
 *   world: true
 *   world_nether: false
 */
public class WorldConstraint extends AbstractConstraint {
        private final List<String> worlds = new ArrayList<String>();

        public WorldConstraint() {}

        @Override
        public void load(ConfigurationSection config) {
                super.load(config);
                for (String key : config.getKeys(false)) {
                        if (config.getBoolean(key)) worlds.add(key);
                }
        }

        @Override
        public boolean isSatisfied(Player player) {
                return worlds.contains(player.getWorld().getName());
        }
}
