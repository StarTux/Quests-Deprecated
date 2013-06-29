package com.winthier.quests.goal;

import com.winthier.quests.item.ItemManager;
import com.winthier.quests.item.QuestItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

/**
 * The MaterialMatcher goal can be used to count the occurence of
 * an item type (material) in a quest.
 */
public class ItemGoal extends QuotaGoal {
        private QuestItem items;

        public ItemGoal() {}

        @Override
        public void load(ConfigurationSection config) {
                super.load(config);
                items = ItemManager.fromStringList(config.getStringList("Items"));
        }

        /**
         * @return The amount of items relevant to the score
         */
        public int feed(Player player, ItemStack stack) {
                if (matches(stack)) {
                        return addScore(player, stack.getAmount());
                }
                return 0;
        }

        public boolean feed(Player player, Material mat) {
                if (matches(mat)) {
                        addScore(player, 1);
                        return true;
                }
                return false;
        }

        public boolean feed(Player player, MaterialData data) {
                if (matches(data)) {
                        addScore(player, 1);
                        return true;
                }
                return false;
        }

        public boolean feed(Player player, Block block) {
                if (matches(block)) {
                        addScore(player, 1);
                        return true;
                }
                return false;
        }

        public boolean matches(ItemStack stack) {
                return items.matches(stack);
        }

        public boolean matches(Material mat) {
                return items.matches(mat);
        }

        public boolean matches(MaterialData data) {
                return items.matches(data);
        }

        public boolean matches(Block block) {
                return items.matches(block);
        }

        @Override
        public String toString() {
                return getClass().getSimpleName() + ":" + items.toString();
        }
}
