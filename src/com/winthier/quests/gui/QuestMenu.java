package com.winthier.quests.gui;

import com.winthier.quests.QuestsPlugin;
import com.winthier.quests.player.PlayerData;
import com.winthier.quests.quest.CategoryInfo;
import com.winthier.quests.quest.Quest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
//import org.bukkit.scheduler.BukkitRunnable;

public class QuestMenu implements Listener {
        public final QuestsPlugin plugin;
        private Map<String, List<String>> itemMap = new HashMap<String, List<String>>();

        public void onEnable() {
                plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }

        public void onDisable() {
        }

        public QuestMenu(QuestsPlugin plugin) {
                this.plugin = plugin;
        }

        public List<ItemStack> getItemsForCategories(PlayerData data, List<String> categories) {
                List<ItemStack> result = new ArrayList<ItemStack>(categories.size());
                for (String category : categories) {
                        CategoryInfo info = plugin.questManager.getCategoryInfo(category);
                        ItemStack item = info.getIcon();
                        int amount = 0;
                        for (Quest quest : plugin.questManager.getQuestList(category)) if (data.isQuestCompleted(quest)) amount++;
                        item.setAmount(amount);
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName("" + ChatColor.YELLOW + info.getDisplayName());
                        List<String> lore = new ArrayList<String>();
                        for (String line : info.getDescription()) {
                                lore.add("" + ChatColor.GRAY + line);
                        }
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        result.add(item);
                }
                return result;
        }

        public void openMenu(Player player) {
                PlayerData data = plugin.playerManager.getPlayerData(player);
                List<String> categories = new ArrayList<String>();
                for (String category : plugin.questManager.getCategories()) {
                        if (!data.getUnlockedCategories().contains(category)) continue;
                        categories.add(category);
                }
                List<ItemStack> items = getItemsForCategories(data, categories);
                int size = (((items.size() - 1) / 9) + 1) * 9;
                Inventory inventory = plugin.getServer().createInventory(player, size, "" + ChatColor.DARK_BLUE + "Quests Menu");
                for (int i = 0; i < items.size(); ++i) {
                        inventory.setItem(i, items.get(i));
                }
                player.openInventory(inventory);
                itemMap.put(player.getName(), categories);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        public void onInventoryClick(InventoryClickEvent event) {
                List<String> items = itemMap.get(event.getWhoClicked().getName());
                if (items == null) return;
                event.setCancelled(true);
                if (!(event.getWhoClicked() instanceof Player)) return;
                Player player = (Player)event.getWhoClicked();
                PlayerData data = plugin.playerManager.getPlayerData(player);
                if (event.getSlot() >= 0 && event.getSlot() < items.size()) {
                        data.setCurrentCategory(items.get(event.getSlot()));
                        Quest quest = data.findCurrentQuest();
                        if (quest != null) quest.displayQuestInfo(player);
                }
                // final InventoryView view = event.getView();
                // new BukkitRunnable() {
                //         public void run() {
                //                 view.close();
                //         }
                // }.runTask(plugin);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        public void onInventoryClose(InventoryCloseEvent event) {
                List<String> items = itemMap.remove(event.getPlayer().getName());
        }
}
