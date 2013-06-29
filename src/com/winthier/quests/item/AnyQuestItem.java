package com.winthier.quests.item;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

/**
 * Match anything
 */
public class AnyQuestItem implements QuestItem {
        public AnyQuestItem() {}
        public boolean matches(Material mat) { return true; }
        public boolean matches(Material mat, int data) { return true; }
        public boolean matches(MaterialData mat) { return true; }
        public boolean matches(ItemStack item) { return true; }
        public boolean matches(Block block) { return true; }
        public Material getMaterial() { return Material.AIR; }
        public int getData() { return 0; }
        public ItemStack toItemStack() { return new ItemStack(Material.AIR); }
}
