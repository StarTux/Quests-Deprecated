package com.winthier.quests.reward;

import com.winthier.quests.quest.Quest;
import com.winthier.quests.util.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.milkbowl.vault.item.ItemInfo;
import net.milkbowl.vault.item.Items;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemReward extends AbstractReward {
        private final List<ItemStack> items = new ArrayList<ItemStack>();

        public ItemReward(Quest quest) {
                super(quest);
        }

        @Override
        public void load(ConfigurationSection config) {
                super.load(config);
                for (String key : config.getKeys(false)) {
                        ItemStack item = Util.parseItemStack(key);
                        if (item == null) {
                                quest.logWarning(config.getCurrentPath() + ": invalid item: " + key);
                                continue;
                        }
                        int amount = config.getInt(key);
                        item.setAmount(amount);
                        items.add(item);
                }
        }

        @Override
        public void give(Player player) {
                for (ItemStack item : items) {
                        // HashMap<Integer, ItemStack> drops = player.getInventory().addItem(item);
                        // for (ItemStack drop : drops.values()) {
                        int amount = item.getAmount();
                        int max = item.getMaxStackSize();
                        while (amount > 0) {
                                int a = Math.min(amount, max);
                                amount -= a;
                                ItemStack clone = item.clone();
                                clone.setAmount(a);
                                player.getWorld().dropItem(player.getEyeLocation(), clone);
                        }
                        // }
                }
        }

        private String getDescription(ItemStack item) {
                ItemInfo info = Items.itemByStack(item);
                String name;
                if (info == null) {
                        name = Util.camelCase("_", item.getType().name()) + ":" + item.getDurability();
                } else {
                        name = info.getName();
                }
                if (item.getAmount() > 1) return name + "x" + item.getAmount();
                return name;
        }

        @Override
        public String getDescription() {
                if (items.isEmpty()) return null;
                StringBuilder sb = new StringBuilder(getDescription(items.get(0)));
                for (int i = 1; i < items.size(); ++i) {
                        ItemStack item = items.get(i);
                        sb.append(", " + getDescription(item));
                }
                return sb.toString();
        }
}
