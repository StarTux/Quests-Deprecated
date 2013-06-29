package com.winthier.quests.goal;

import org.bukkit.entity.Player;

/**
 * This is a goal which never changes.  It will always show the
 * same box info and not store any data in the
 * configuration. Subsequently, it can't be completed.
 */
public class DummyGoal extends AbstractGoal {
        @Override
        public String getBoxInfo(Player player) {
                return " ";
        }

        @Override
        public boolean isCompleted(Player player) {
                return false;
        }
}
