package com.winthier.quests.goal;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class FurnaceExtractGoal extends ItemGoal {
        /**
         * Bukkit can't get shift clicks in furnaces right, so the
         * FurnaceExtractEvent yields 0 amounts. For these cases,
         * we have to investigate the InventoryClickEvent.
         * Being too lazy to reimplement vanilla inventory checks,
         * we have to hope that no one figures out the possible
         * exploits when the stack can't be put in one's
         * inventory.
         */
        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onInventoryClick(InventoryClickEvent event) {
                if (!(event.getWhoClicked() instanceof Player)) return;
                Player player = (Player)event.getWhoClicked();
                if (!checkConstraints(player)) return;
                if (event.getInventory().getType() != InventoryType.FURNACE) return;
                if (event.getSlotType() != InventoryType.SlotType.RESULT) return;
                if (!event.isShiftClick()) return;
                ItemStack item = event.getCurrentItem();
                if (item == null) return;
                feed(player, item);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onFurnaceExtract(FurnaceExtractEvent event) {
                if (event.getItemAmount() == 0) return;
                Player player = event.getPlayer();
                if (!checkConstraints(player)) return;
                if (matches(event.getItemType())) {
                        addScore(player, event.getItemAmount());
                }
        }
}
