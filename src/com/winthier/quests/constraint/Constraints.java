package com.winthier.quests.constraint;

import com.winthier.quests.util.Util;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * This is the recursive type of Constraint which can contain
 * other Constraints. It is intended to be used as the root of a
 * constraints tree and to clarify the semantics of constraints
 * within a quest, meaning that all of them have to be satisfied
 * for progress to be allowed for a player.
 */
public class Constraints extends AbstractConstraint {
        private final List<Constraint> constraints = new ArrayList<Constraint>();

        public Constraints() {}

        @Override
        public void load(ConfigurationSection config) {
                if (config == null) return;
                super.load(config);
                for (String key : config.getKeys(false)) {
                        Constraint constraint = ConstraintType.fromString(key);
                        if (constraint == null) {
                                Util.logWarning(config.getCurrentPath() + "." + key + ": invalid constraint");
                                continue;
                        }
                        constraint.load(config.getConfigurationSection(key));
                        constraints.add(constraint);
                }
        }

        @Override
        public boolean isSatisfied(Player player) {
                for (Constraint constraint : constraints) {
                        if (!constraint.isSatisfied(player)) return false;
                }
                return true;
        }
}
