package com.winthier.quests.goal;

import com.winthier.quests.util.EnchantmentMatcher;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

class EnchantItemGoal extends ItemGoal {
        private List<EnchantmentMatcher> enchantments;
        private int expLevel;

        public EnchantItemGoal() {}

        public void load(ConfigurationSection config) {
                super.load(config);
                enchantments = EnchantmentMatcher.fromConfig(config, "Enchantments");
                expLevel = config.getInt("ExpLevel", 0);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onEnchantItem(EnchantItemEvent event) {
                Player player = event.getEnchanter();
                if (!checkConstraints(player)) return;
                feed(player, event.getItem(), event.getEnchantsToAdd(), event.getExpLevelCost());
        }

        public int feed(Player player, ItemStack item, Map<Enchantment, Integer> enchantments, int expLevel) {
                if (this.expLevel > expLevel) return 0;
                if (!super.matches(item)) return 0;
                if (!this.enchantments.isEmpty()) {
                        boolean matches = false;
                        for (EnchantmentMatcher matcher : this.enchantments) {
                                if (matcher.matches(enchantments)) {
                                        matches = true;
                                        break;
                                }
                        }
                        if (!matches) return 0;
                }
                return addScore(player, item.getAmount());
        }
}
