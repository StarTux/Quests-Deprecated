package com.winthier.quests.quest;

import com.winthier.quests.QuestsPlugin;
import com.winthier.quests.constraint.Constraints;
import com.winthier.quests.effect.Effect;
import com.winthier.quests.effect.EffectType;
import com.winthier.quests.goal.AbstractGoal;
import com.winthier.quests.goal.Goal;
import com.winthier.quests.goal.GoalType;
import com.winthier.quests.player.PlayerData;
import com.winthier.quests.reward.Rewards;
import com.winthier.quests.util.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Quest {
        public final QuestsPlugin plugin;
        protected String category, name;
        protected String title;
        protected List<String> description;
        protected final Map<String, PlayerData> players = new HashMap<String, PlayerData>();
        protected final List<Goal> goals = new ArrayList<Goal>();
        protected final List<Effect> effects = new ArrayList<Effect>();
        protected final Rewards reward, hiddenReward;
        protected final Constraints constraint;
        protected Location compassTarget;

        public Quest(QuestsPlugin plugin) {
                this.plugin = plugin;
                constraint = new Constraints();
                reward = new Rewards(this);
                hiddenReward = new Rewards(this);
        }

        public void load(ConfigurationSection section) {
                ConfigurationSection data, messages, rewards, constraints, goals;
                ConfigurationSection defaultGoal = section.getConfigurationSection("default");
                loadData(section.getConfigurationSection("data"));
                loadEffects(section.getConfigurationSection("effects"));
                loadGoals(section.getConfigurationSection("goals"));
                loadMessages(section.getConfigurationSection("messages"));
                reward.load(section.getConfigurationSection("rewards"));
                hiddenReward.load(section.getConfigurationSection("hidden-rewards"));
                loadConstraints(section.getConfigurationSection("constraints"));
                String compassTarget = section.getString("CompassTarget");
                if (compassTarget != null) {
                        this.compassTarget = Util.parseLocation(compassTarget);
                        if (this.compassTarget == null) {
                                logWarning(section.getCurrentPath() + ": invalid location: " + compassTarget);
                        }
                }
        }

        public final void setCategoryAndName(String category, String name) {
                this.category = category;
                this.name = name;
        }

        public final String getCategory() {
                return category;
        }

        public final String getName() {
                return name;
        }

        public boolean isCompleted(Player player) {
                return plugin.playerManager.getPlayerData(player).isQuestCompleted(this);
        }

        public void setCompleted(Player player, boolean completed) {
                plugin.playerManager.getPlayerData(player).setQuestCompleted(this, true);
        }

        final public void addPlayer(PlayerData playerData) {
                players.put(playerData.getName(), playerData);
                if (compassTarget != null) {
                        Player player = playerData.getPlayer();
                        if (player != null) player.setCompassTarget(compassTarget);
                }
        }

        final public void removePlayer(PlayerData playerData) {
                if (compassTarget != null) {
                        Player player = playerData.getPlayer();
                        if (player != null) player.setCompassTarget(plugin.getServer().getWorlds().get(0).getSpawnLocation());
                }
                players.remove(playerData.getName());
        }

        final public boolean hasPlayer(Player player) {
                return players.keySet().contains(player.getName());
        }

        final public void savePlayer(PlayerData playerData) {
                onPlayerSave(playerData);
        }

        public ConfigurationSection getPlayerQuestData(PlayerData playerData) {
                return playerData.getQuestData(this);
        }

        public ConfigurationSection getPlayerQuestData(Player player) {
                return plugin.playerManager.getPlayerData(player).getQuestData(this);
        }

        public boolean goalsCompleted(Player player) {
                for (Goal goal : goals) {
                        if (!goal.isCompleted(player)) return false;
                }
                return true;
        }

        /**
         * This should be called when the quest is considered
         * complete. No further checks are made!
         */
        public void completeQuest(final Player player) {
                setCompleted(player, true);
                onComplete(player);
                reward.give(player);
                hiddenReward.give(player);
                final PlayerData playerData = plugin.playerManager.getPlayerData(player);
                new BukkitRunnable() {
                        public void run() {
                                playerData.updateCurrentQuest();
                        }
                }.runTask(plugin);
                new BukkitRunnable() {
                        public void run() {
                                Quest quest = playerData.getCurrentQuest();
                                if (!playerData.seen && quest != null && !quest.isCompleted(player)) {
                                        quest.displayQuestInfo(player);
                                }
                        }
                }.runTaskLater(plugin, plugin.autoUpdateInterval * 20L);
        }

        /**
         * Check whether all constraints are satisfied.
         * Subclasses should check this before they attempt to
         * make any player progress.
         * Also check if the player is even in this quest.
         */
        public boolean checkConstraints(Player player) {
                if (!hasPlayer(player)) return false;
                return constraint.isSatisfied(player);
        }

        // Loading routines

        public void loadData(ConfigurationSection data) {}

        public void loadEffects(ConfigurationSection config) {
                if (config == null) return;
                for (String key : config.getKeys(false)) {
                        ConfigurationSection section = config.getConfigurationSection(key);
                        String effectType = section.getString("Type", "null");
                        Effect effect = EffectType.fromString(effectType);
                        if (effect == null) {
                                logWarning(section.getCurrentPath() + ": Invalid Type: " + effectType);
                                continue;
                        }
                        effect.setQuestAndKey(this, key);
                        effects.add(effect);
                        effect.load(section);
                        effect.enable();
                }
        }

        public void loadGoals(ConfigurationSection goals) {
                if (goals == null) return;
                for (String key : goals.getKeys(false)) {
                        ConfigurationSection goalSection = goals.getConfigurationSection(key);
                        Goal goal = null;
                        String goalType = goalSection.getString("Type");
                        if (goalType != null) goal = GoalType.fromString(goalType);
                        if (goal == null) {
                                logWarning(goals.getCurrentPath() + "." + key + ": invalid goal type: " + goalType);
                                continue;
                        }
                        goal.setQuestAndKey(this, key);
                        goal.load(goalSection);
                        this.goals.add(goal);
                        goal.enable();
                }
        }

        public void loadMessages(ConfigurationSection messages) {
                if (messages == null) return;
                title = messages.getString("Title", "");
                description = Util.getStringList(messages.get("Description"));
        }

        public void loadConstraints(ConfigurationSection constraints) {
                if (constraints == null) return;
                constraint.load(constraints);
        }

        public void enable() {}

        // Output routines

        private void displayHorizontalLine(Player p) {
                Util.print(p, "&8+&m                                                                 ");
        }

        private void displayBoxedText(Player p, String msg) {
                Util.print(p, "&8| " + msg);
        }

        private void displayBoxedTitle(Player p, String quest, String category) {
                displayBoxedText(p, "" + ChatColor.AQUA + ChatColor.UNDERLINE + quest + ChatColor.DARK_AQUA + " " + category);
        }

        public void displayHelp(Player p) {
                // TODO
        }

        public void displayQuestInfo(Player p) {
                plugin.playerManager.getPlayerData(p).seen = true;
                displayHorizontalLine(p);
                displayBoxedTitle(p, title, plugin.questManager.getCategoryInfo(category).getDisplayName());
                displayHorizontalLine(p);
                for (String line : description) {
                        displayBoxedText(p, "" + ChatColor.GRAY + line);
                }
                for (Goal goal : goals) {
                        displayBoxedText(p, "&7[&b" + goal.getBoxInfo(p) + "&7] &b" + goal.getTitle());
                }
                String rewards = reward.getDescription();
                if (rewards.length() > 0) displayBoxedText(p, "&8Rewards: &7" + reward.getDescription());
                displayHorizontalLine(p);
        }

        public void displayQuestCompletion(Player p) {
                displayHorizontalLine(p);
                displayBoxedTitle(p, title, plugin.questManager.getCategoryInfo(category).getDisplayName());
                displayHorizontalLine(p);
                for (Goal goal : goals) {
                        displayBoxedText(p, "&8[" + goal.getBoxInfo(p) + "] " + goal.getTitle());
                }
                displayBoxedText(p, "&a&lQuest Complete!");
                String rewards = reward.getDescription();
                if (rewards.length() > 0) displayBoxedText(p, "&8Rewards: &b" + reward.getDescription());
                displayHorizontalLine(p);
        }

        public void displayGoalCompletion(AbstractGoal goal, Player p) {
                displayHorizontalLine(p);
                displayBoxedText(p, "" + ChatColor.GREEN + "[" + goal.getBoxInfo(p) + "] " + goal.getTitle());
                displayHorizontalLine(p);
        }

        // Logging

        public void logWarning(String msg) {
                plugin.getLogger().warning(getClass().getSimpleName() + ": " + msg);
        }

        public void logInfo(String msg) {
                plugin.getLogger().info(getClass().getSimpleName() + ": " + msg);
        }

        // Hooks

        protected void onPlayerSave(PlayerData data) {}
        /**
         *
         */
        public void onGoalAdvance(AbstractGoal goal, Player player, float percentage) {
                plugin.protocol.playSound(player, "random.orb", player.getEyeLocation(), 0.67f, (int)(127.0f * percentage));
        }
        /**
         * Called when a goal is completed.
         * This default implementation checks for quest completion.
         */
        public void onGoalComplete(AbstractGoal goal, Player player) {
                if (isCompleted(player)) return;
                plugin.protocol.playSound(player, "random.levelup", player.getEyeLocation(), 0.67f, 63);
                if (goalsCompleted(player)) {
                        completeQuest(player);
                } else {
                        displayGoalCompletion(goal, player);
                }
        }
        /**
         * Called when the quest is completed
         * This default implementation prints a quest completion
         * screen in the chat.
         */
        public void onComplete(Player player) {
                displayQuestCompletion(player);
        }
}
