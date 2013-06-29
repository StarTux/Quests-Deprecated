package com.winthier.quests.goal;

import com.winthier.quests.event.DeliveryChestEvent;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Deliver an item to an AdviceAnimal. This goal requires a
 * corresponding DeliveryChestEffect to be present in the same
 * Quest.
 */
public class DeliverItemGoal extends ItemGoal {
        private List<String> animalNames;

        public DeliverItemGoal() {}

        @Override
        public void load(ConfigurationSection data) {
                super.load(data);
                animalNames = data.getStringList("AnimalNames");
        }

        /**
         * Count the items that match what is needed, up to the
         * needed score to fulfill the quota, and remove them from
         * the inventory.
         */
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        public void onDeliveryChest(DeliveryChestEvent event) {
                Player player = (Player)event.getPlayer();
                if (!checkConstraints(player)) return;
                Inventory inventory = event.getInventory();
                int needed = quota - getScore(player);
                int found = 0;
                if (needed <= 0) return;
                for (int i = 0; i < inventory.getSize() && needed > found; ++i) {
                        ItemStack item = inventory.getItem(i);
                        if (!matches(item)) continue;
                        int count = Math.min(item.getAmount(), needed - found);
                        int newAmount = item.getAmount() - count;
                        found += count;
                        if (newAmount <= 0) {
                                inventory.setItem(i, null);
                        } else {
                                item.setAmount(newAmount);
                        }
                }
                addScore(player, found);
        }
}
