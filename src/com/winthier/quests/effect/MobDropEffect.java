package com.winthier.quests.effect;

import org.bukkit.configuration.ConfigurationSection;

/**
 * This effect causes mobs to drop different or additional items
 * when killed.
 *
 * Configuration:
 * mobdrop:
 * - Entities: [ Creeper ] # Condition
 *   Chance: 0.1 # Condition: drop chance (1.0 = always)
 *   Clear: false # Clear natural drops (if all conditions hold true)
 *   items:
 *     Diamond: 1
 */
public class MobDropEffect extends AbstractEffect {
        @Override
        public void enable() {}

        @Override
        public void load(ConfigurationSection config) {
                
        }
}
