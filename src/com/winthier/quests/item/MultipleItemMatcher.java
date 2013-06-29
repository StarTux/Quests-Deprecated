package com.winthier.quests.item;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class MultipleItemMatcher implements QuestItem {
        private final List<QuestItem> list;

        public MultipleItemMatcher() {
                list = new ArrayList<QuestItem>();
        }

        public MultipleItemMatcher(List<QuestItem> list) {
                this.list = list;
        }

        public void add(QuestItem item) {
                list.add(item);
        }

        @Override
        public boolean matches(Material mat) {
                for (QuestItem matcher : list) if (matcher.matches(mat)) return true;
                return false;
        }

        @Override
        public boolean matches(Material mat, int data) {
                for (QuestItem matcher : list) if (matcher.matches(mat, data)) return true;
                return false;
        }

        @Override
        public boolean matches(MaterialData mat) {
                for (QuestItem matcher : list) if (matcher.matches(mat)) return true;
                return false;
        }

        @Override
        public boolean matches(ItemStack item) {
                for (QuestItem matcher : list) if (matcher.matches(item)) return true;
                return false;
        }

        @Override
        public boolean matches(Block block) {
                for (QuestItem matcher : list) if (matcher.matches(block)) return true;
                return false;
        }

        @Override
        public Material getMaterial() {
                return Material.AIR;
        }

        @Override
        public int getData() {
                return 0;
        }

        @Override
        public ItemStack toItemStack() {
                return new ItemStack(Material.AIR);
        }
}
