package com.winthier.quests.item;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class MaterialDataMatcher extends MaterialMatcher {
        public final int data;

        public MaterialDataMatcher(Material material, int data) {
                super(material);
                this.data = data;
        }

        @Override
        public boolean matches(Material mat, int data) {
                if (mat == null) return false;
                return super.matches(mat) && this.data == data;
        }

        @Override
        public boolean matches(MaterialData mat) {
                if (mat == null) return false;
                return super.matches(mat) && this.data == (int)mat.getData();
        }

        @Override
        public boolean matches(ItemStack item) {
                if (item == null) return false;
                return super.matches(item) && this.data == (short)item.getDurability();
        }

        @Override
        public boolean matches(Block block) {
                if (block == null) return false;
                return super.matches(block) && this.data == (int)block.getData();
        }

        @Override
        public int getData() {
                return data;
        }

        @Override
        public ItemStack toItemStack() {
                return new ItemStack(material, 1, (short)data);
        }
}
