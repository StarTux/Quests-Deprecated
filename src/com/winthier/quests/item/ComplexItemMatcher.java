package com.winthier.quests.item;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class ComplexItemMatcher implements QuestItem {
        private final QuestItem base;
        private String name;
        private List<String> lore;

        private ComplexItemMatcher(QuestItem base) {
                this.base = base;
        }

        public static ComplexItemMatcher create(QuestItem base, String tokens[]) {
                ComplexItemMatcher result = new ComplexItemMatcher(base);
                for (String token : tokens) {
                        if (token.startsWith("name=")) {
                                result.name = ChatColor.translateAlternateColorCodes('&', token.substring(5));
                        } else if (token.startsWith("lore=")) {
                                result.lore = Arrays.asList(ChatColor.translateAlternateColorCodes('&', token.substring(5)).split(Pattern.quote("\\n")));
                        }
                }
                return result;
        }

        @Override
        public boolean matches(Material mat) {
                if (mat == null) return false;
                return base.matches(mat);
        }

        @Override
        public boolean matches(Material mat, int data) {
                if (mat == null) return false;
                return base.matches(mat, data);
        }

        @Override
        public boolean matches(MaterialData mat) {
                if (mat == null) return false;
                return base.matches(mat);
        }

        @Override
        public boolean matches(ItemStack item) {
                if (item == null) return false;
                if (!base.matches(item)) return false;
                ItemMeta meta = item.getItemMeta();
                if (name != null) {
                        if (!meta.hasDisplayName()) return false;
                        if (!name.equals(meta.getDisplayName())) return false;
                }
                if (lore != null) {
                        if (!meta.hasLore()) return false;
                        if (!lore.equals(meta.getLore())) return false;
                }
                return true;
        }

        @Override
        public boolean matches(Block block) {
                if (block == null) return false;
                return base.matches(block);
        }

        @Override
        public Material getMaterial() {
                return base.getMaterial();
        }

        @Override
        public int getData() {
                return base.getData();
        }

        @Override
        public ItemStack toItemStack() {
                ItemStack result = base.toItemStack();
                ItemMeta meta = result.getItemMeta();
                if (name != null) {
                        meta.setDisplayName(name);
                }
                if (lore != null) {
                        meta.setLore(lore);
                }
                result.setItemMeta(meta);
                return result;
        }
}
