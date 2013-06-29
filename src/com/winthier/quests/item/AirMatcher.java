package com.winthier.quests.item;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

/**
 * Matches air
 */
public class AirMatcher implements QuestItem {
        public AirMatcher() {}

        public boolean matches(Material mat) {
                return mat == null || mat == Material.AIR;
        }

        public boolean matches(Material mat, int data) {
                return mat == null || mat == Material.AIR;
        }

        public boolean matches(MaterialData mat) {
                return mat == null || mat.getItemType() == Material.AIR;
        }

        public boolean matches(ItemStack item) {
                return item == null || item.getType() == Material.AIR;
        }

        public boolean matches(Block block) {
                return block == null || block.getType() == Material.AIR;
        }

        public Material getMaterial() {
                return Material.AIR;
        }

        public int getData() {
                return 0;
        }

        public ItemStack toItemStack() {
                return new ItemStack(Material.AIR);
        }
}
