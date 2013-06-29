package com.winthier.quests.goal;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * A QuotaGoal wants the player to get a number up to a certain
 * level.
 */
public class QuotaGoal extends AbstractGoal {
        protected int quota;
        protected boolean hideScore;

        public QuotaGoal() {}

        @Override
        public boolean isCompleted(Player player) {
                return getScore(player) >= quota;
        }

        public int getScore(Player player) {
                return Math.min(quota, quest.getPlayerQuestData(player).getInt(key, 0));
        }

        public void setScore(Player player, int score) {
                int oldScore = quest.getPlayerQuestData(player).getInt(key, 0);
                quest.getPlayerQuestData(player).set(key, score);
                if (oldScore < quota && score >= quota) quest.onGoalComplete(this, player);
                else if (oldScore < score) quest.onGoalAdvance(this, player, (float)score / (float)quota);
        }

        /**
         * @return the actual score added
         */
        public int addScore(Player player, int score) {
                if (score == 0) return 0;
                ConfigurationSection section = quest.getPlayerQuestData(player);
                int oldScore = Math.min(quota, section.getInt(key, 0));
                int newScore = Math.min(quota, score + oldScore);
                newScore = Math.max(0, newScore);
                section.set(key, newScore);
                if (oldScore < quota && newScore >= quota) quest.onGoalComplete(this, player);
                else if (oldScore < newScore) quest.onGoalAdvance(this, player, (float)oldScore / (float)quota);
                return newScore - oldScore;
        }

        @Override
        public String getBoxInfo(Player player) {
                int score = getScore(player);
                if (hideScore || quota == 1) {
                        return score >= quota ? "\u2713" : " ";
                } else {
                        if (score >= quota) return "\u2713";
                        return "" + Math.min(getScore(player), quota) + "/" + quota;
                }
        }

        @Override
        public void load(ConfigurationSection data) {
                super.load(data);
                quota = data.getInt("Quota", 1);
                hideScore = data.getBoolean("HideScore", false);
        }
}
