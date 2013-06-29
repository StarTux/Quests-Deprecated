package com.winthier.quests.constraint;

import org.bukkit.configuration.ConfigurationSection;

/**
 * An abstract implementation of Constraint, defining the
 * constructor signature to be used for any constraint, according
 * to ConstraintType.
 */
public abstract class AbstractConstraint implements Constraint {
        public AbstractConstraint() {}

        @Override
        public void load(ConfigurationSection config) {}

        public void logWarning(String msg) {
                System.err.println("[Quests] " + getClass().getSimpleName() + ": " + msg);
        }
}
