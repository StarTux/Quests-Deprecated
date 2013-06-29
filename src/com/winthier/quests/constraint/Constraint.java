package com.winthier.quests.constraint;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * Constraints are general conditions that can be placed on any
 * quest and have to be true for the affected player to be allowed
 * to progress in the specific quest. A sensible example is that
 * the goals of a quest have to be satisfied within a certain
 * region.
 *
 * Constraints are intended to be checked by the specific quests,
 * ideally by calling a constraint management function in a
 * superclass, before adding score to any goals.
 *
 * Constraints are not meant to be displayed to the player but
 * instead explained in the context of the quest.
 *
 * Quest has a function checkConstraints(Player) to take
 * care of that.
 */
public interface Constraint {
        public boolean isSatisfied(Player player);
        public void load(ConfigurationSection config);
}
