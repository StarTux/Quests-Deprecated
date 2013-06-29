package com.winthier.quests.effect;

import com.winthier.adviceanimals.AdviceAnimal;
import com.winthier.adviceanimals.event.AdviceAnimalEvent;
import com.winthier.quests.event.DeliveryChestEvent;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

/**
 * This effect opens a chest interface to participating
 * players. Closing the chest causes the relevant
 * DeliveryChestEvent to be called. This is intended to be used in
 * conjunction with the DeliverItemGoal.
 *
 * Configuration:
 * deliverychest:
 *   Type: DeliveryChest
 *   AnimalName: WelcomeCat # AdviceAnimal name
 *   Size: 18
 *   Title: 'Fish?'
 */
public class DeliveryChestEffect extends AbstractEffect {
        private static OpenInventoryWatcher openInventoryWatcher;
        private String animalName, title;
        private int size;

        @Override
        public void load(ConfigurationSection config) {
                animalName = config.getString("AnimalName", "Animal");
                title = config.getString("Title");
                size = config.getInt("ChestSize", 9);
                if (size % 9 != 0) {
                        logWarning(config.getCurrentPath() + ".ChestSize: Must be a multiple of nine, got: " + size);
                        size = ((size - 1) / 9 + 1) * 9;
                }
        }

        @Override
        public void enable() {
                super.enable();
                if (openInventoryWatcher == null) {
                        openInventoryWatcher = new OpenInventoryWatcher();
                        quest.plugin.getServer().getPluginManager().registerEvents(openInventoryWatcher, quest.plugin);
                }
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onAdviceAnimal(AdviceAnimalEvent event) {
                Player player = event.getPlayer();
                if (!checkConstraints(player)) return;
                AdviceAnimal animal = event.getAdviceAnimal();
                if (!animalName.equals(animal.getName())) return;
                String chestTitle = animal.getPrefix();
                if (title.length() > 0) {
                        chestTitle = chestTitle + ChatColor.BLACK + " " + title;
                }
                chestTitle = ChatColor.translateAlternateColorCodes('&', chestTitle);
                if (chestTitle.length() > 32) chestTitle = chestTitle.substring(0, 32);
                Inventory inventory;
                try {
                        inventory = quest.plugin.getServer().createInventory(player, size, chestTitle);
                } catch (IllegalArgumentException iae) {
                        iae.printStackTrace();
                        return;
                }
                player.openInventory(inventory);
                openInventoryWatcher.animals.put(player.getName(), animal);
                event.setCancelled(true);
        }

}

class OpenInventoryWatcher implements Listener {
        public final Map<String, AdviceAnimal> animals = new HashMap<String, AdviceAnimal>();

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onInventoryClose(InventoryCloseEvent event) {
                AdviceAnimal animal = animals.remove(event.getPlayer().getName());
                if (animal == null) return;
                Inventory inventory = event.getInventory();
                Player player = (Player)event.getPlayer();
                Bukkit.getServer().getPluginManager().callEvent(new DeliveryChestEvent(player, animal, inventory));
                for (ItemStack item : inventory) {
                        if (item == null || item.getType() == Material.AIR) continue;
                        player.getWorld().dropItem(player.getEyeLocation(), item);
                }
        }
}
