package com.winthier.quests.goal;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class CraftItemGoal extends ItemGoal {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onCraftItem(CraftItemEvent event) {
                if (!(event.getWhoClicked() instanceof Player)) return;
                Player player = (Player)event.getWhoClicked();
                if (!checkConstraints(player)) return;
                feed(player, event.getCurrentItem());
        }
}
