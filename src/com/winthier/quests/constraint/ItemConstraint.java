package com.winthier.quests.constraint;

import com.winthier.quests.item.ItemManager;
import com.winthier.quests.item.QuestItem;
import com.winthier.quests.util.Util;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Check if a player has certain items. He has to have all of the
 * specified item amounts. Specifying an amount of 0 means that
 * the item must not be contained.
 *
 * Configuration:
 * item:
 *   gold_ingot: 32 # Must have 32 gold ingots
 *   diamond: 0 # Mustn't have any diamonds
 */
public class ItemConstraint extends AbstractConstraint {
        private List<QuestItem> items = new ArrayList<QuestItem>();
        private List<Integer> amounts = new ArrayList<Integer>();

        public ItemConstraint() {}

        @Override
        public void load(ConfigurationSection config) {
                for (String key : config.getKeys(false)) {
                        QuestItem item = ItemManager.fromString(key);
                        if (item == null) {
                                Util.logWarning(config.getCurrentPath() + ": Invalide item: " + key);
                                continue;
                        }
                        int amount = config.getInt(key);
                        items.add(item);
                        amounts.add(amount);
                }
        }

        @Override
        public boolean isSatisfied(Player player) {
                for (int i = 0; i < items.size(); ++i) {
                        QuestItem matcher = items.get(i);
                        int amount = amounts.get(i);
                        int found = 0;
                itemLoop:
                        for (ItemStack item : player.getInventory()) {
                                if (matcher.matches(item)) {
                                        if (amount == 0) return false;
                                        found += item.getAmount();
                                        if (found >= amount) break itemLoop;
                                }
                        }
                        if (found < amount) return false;
                }
                return true;
        }
}
