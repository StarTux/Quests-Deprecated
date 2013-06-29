package com.winthier.quests.item;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class MaterialMatcher implements QuestItem {
        public final Material material;

        public MaterialMatcher(Material material) {
                this.material = material;
        }

        @Override
        public boolean matches(Material mat) {
                if (mat == null) return false;
                return this.material == mat;
        }

        @Override
        public boolean matches(Material mat, int data) {
                if (mat == null) return false;
                return this.material == mat;
        }

        @Override
        public boolean matches(MaterialData mat) {
                if (mat == null) return false;
                return this.material == mat.getItemType();
        }

        @Override
        public boolean matches(ItemStack item) {
                if (item == null) return false;
                return this.material == item.getType();
        }

        @Override
        public boolean matches(Block block) {
                if (block == null) return false;
                return this.material == block.getType();
        }

        @Override
        public Material getMaterial() {
                return material;
        }

        @Override
        public int getData() {
                return 0;
        }

        @Override
        public ItemStack toItemStack() {
                return new ItemStack(material);
        }
}
