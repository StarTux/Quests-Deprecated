package com.winthier.quests.reward;

import com.winthier.quests.quest.Quest;
import com.winthier.quests.util.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * The RankReward attempts to rank a player from one rank to
 * another, presumably higher one. It uses Vault, which calls
 * ranks groups.
 *
 * A "from" group can be specified. If the receiving player is not
 * in that group, nothing will happen.
 *
 * Configuration:
 * rank:
 *   From: 'default'
 *   To: 'member'
 *   Name: 'Member Rank' # The display name (optional)
 */
public class RankReward extends AbstractReward {
        private String from, to, name;

        public RankReward(Quest quest) {
                super(quest);
        }

        @Override
        public void load(ConfigurationSection config) {
                super.load(config);
                from = config.getString("From");
                to = config.getString("To");
                name = config.getString("Name");
                if (to == null) {
                        Util.logWarning(config.getCurrentPath() + ": missing key: `To'");
                }
        }

        @Override
        public void give(Player player) {
                if (to == null) return;
                if (from != null) { // check if the player is in the "from" group
                        String groups[] = quest.plugin.vault.permission.getPlayerGroups(player);
                        boolean has = false;
                        for (String group : groups) {
                                if (group.equalsIgnoreCase(from)) {
                                        has = true;
                                        break;
                                }
                        }
                        if (!has) return;
                }
                quest.plugin.vault.permission.playerRemoveGroup(player, from);
                quest.plugin.vault.permission.playerAddGroup(player, to);
        }

        @Override
        public String getDescription() {
                return "Rank: " + to;
        }
}
