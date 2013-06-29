package com.winthier.quests.goal;

import com.winthier.quests.item.ItemManager;
import com.winthier.quests.item.QuestItem;
import com.winthier.simpleshop.event.SimpleShopEvent;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

class SimpleShopGoal extends QuotaGoal {
        private QuestItem items;
        private List<String> shopOwners;
        private boolean selling = true;

        public SimpleShopGoal() {}

        @Override
        public void load(ConfigurationSection config) {
                super.load(config);
                items = ItemManager.fromStringList(config.getStringList("Items"));
                shopOwners = config.getStringList("ShopOwners");
                String shopType = config.getString("ShopType", "Selling");
                if (shopType.equalsIgnoreCase("Buying")) {
                        selling = false;
                } else if (shopType.equalsIgnoreCase("Selling")) {
                        selling = true;
                } else {
                        quest.logWarning(config.getCurrentPath() + ".ShopType: invalid value, Selling|Buying expected. Found: " + shopType);
                }
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onSimpleShop(SimpleShopEvent event) {
                Player player = event.getPlayer();
                if (!checkConstraints(player)) return;
                feed(player, event);
        }

        public boolean matchItem(ItemStack item) {
                return items.matches(item);
        }

        public boolean matchOwner(String ownerName) {
                if (shopOwners.isEmpty()) return true;
                for (String owner : shopOwners) {
                        if (ownerName.equalsIgnoreCase(owner)) return true;
                }
                return false;
        }

        public boolean feed(Player player, SimpleShopEvent event) {
                if (selling != event.getShopChest().isSellingChest()) return false;
                if (matchItem(event.getItem()) && matchOwner(event.getShopChest().getOwnerName())) {
                        addScore(player, event.getItem().getAmount());
                        return true;
                }
                return false;
        }
}
