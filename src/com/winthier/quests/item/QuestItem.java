package com.winthier.quests.item;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

/**
 * An interface for storing infomration about, matching and
 * creating, items.
 */
public interface QuestItem {
        public boolean matches(Material mat);
        public boolean matches(Material mat, int data);
        public boolean matches(MaterialData mat);
        public boolean matches(ItemStack item);
        public boolean matches(Block block);
        public Material getMaterial();
        public int getData();
        public ItemStack toItemStack();
}
