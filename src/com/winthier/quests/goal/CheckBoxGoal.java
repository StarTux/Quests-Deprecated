package com.winthier.quests.goal;

import org.bukkit.entity.Player;

/**
 * The CheckBoxGoal is either completed or not. Completed goals
 */
public class CheckBoxGoal extends AbstractGoal {
        public void setCompleted(Player player, boolean completed) {
                quest.getPlayerQuestData(player).set(key, completed);
                if (completed) quest.onGoalComplete(this, player);
        }

        @Override
        public boolean isCompleted(Player player) {
                return quest.getPlayerQuestData(player).getBoolean(key, false);
        }

        @Override
        public String getBoxInfo(Player player) {
                return isCompleted(player) ? "\u2713" : " ";
        }
}
