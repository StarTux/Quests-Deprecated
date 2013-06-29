package com.winthier.quests.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class EnchantmentMatcher {
        private Enchantment enchantment;
        private int minLevel;
        private static Map<String, Enchantment> nameMap = new HashMap<String, Enchantment>();

        public EnchantmentMatcher(Enchantment enchantment, int minLevel) {
                this.enchantment = enchantment;
                this.minLevel = minLevel;
        }

        public static void addMapping(Enchantment enchantment, String name) {
                nameMap.put(name.toUpperCase().replaceAll("_|-| ", ""), enchantment);
        }

        static {
                for (Enchantment enchantment : Enchantment.values()) {
                        addMapping(enchantment, enchantment.getName());
                }
                addMapping(Enchantment.ARROW_DAMAGE, "Power");
                addMapping(Enchantment.ARROW_FIRE, "Flame");
                addMapping(Enchantment.ARROW_INFINITE, "Infinity");
                addMapping(Enchantment.ARROW_KNOCKBACK, "Knockback");
                addMapping(Enchantment.DAMAGE_ALL, "Sharpness");
                addMapping(Enchantment.DAMAGE_ARTHROPODS, "Bane of Arthropods");
                addMapping(Enchantment.DAMAGE_UNDEAD, "Smite");
                addMapping(Enchantment.DIG_SPEED, "Efficiency");
                addMapping(Enchantment.DURABILITY, "Unbreaking");
                addMapping(Enchantment.FIRE_ASPECT, "Fire Aspect");
                addMapping(Enchantment.KNOCKBACK, "Knockback");
                addMapping(Enchantment.LOOT_BONUS_BLOCKS, "Fortune");
                addMapping(Enchantment.LOOT_BONUS_MOBS, "Looting");
                addMapping(Enchantment.OXYGEN, "Respiration");
                addMapping(Enchantment.PROTECTION_ENVIRONMENTAL, "Protection");
                addMapping(Enchantment.PROTECTION_EXPLOSIONS, "Blast Protection");
                addMapping(Enchantment.PROTECTION_FALL, "Feather Falling");
                addMapping(Enchantment.PROTECTION_FIRE, "Fire Protection");
                addMapping(Enchantment.PROTECTION_PROJECTILE, "Projectile Protection");
                addMapping(Enchantment.SILK_TOUCH, "Silk Touch");
                addMapping(Enchantment.THORNS, "Thorns");
                addMapping(Enchantment.WATER_WORKER, "Water Affinity");
        }

        public static Enchantment findEnchantment(String name) {
                return nameMap.get(name);
                
        }

        public static EnchantmentMatcher fromString(String string) {
                String tokens[] = string.split(":", 2);
                Enchantment enchantment = findEnchantment(tokens[0]);
                int minLevel = 0;
                if (tokens.length >= 2) {
                        try {
                                minLevel = Integer.parseInt(tokens[1]);
                        } catch (NumberFormatException nfe) {
                                return null;
                        }
                }
                return new EnchantmentMatcher(enchantment, minLevel);
        }

        public static List<EnchantmentMatcher> fromConfig(ConfigurationSection config, String key) {
                List<EnchantmentMatcher> result = new ArrayList<EnchantmentMatcher>();
                for (String item : config.getStringList(key)) {
                        EnchantmentMatcher matcher = fromString(item);
                        if (matcher == null) {
                                Util.logWarning(config.getCurrentPath() + "." + key + ": invalid enchantment: " + item);
                                continue;
                        }
                        result.add(matcher);
                }
                return result;
        }

        public boolean matches(Enchantment enchantment) {
                return this.enchantment.equals(enchantment);
        }

        public boolean matches(Map<Enchantment, Integer> enchantments) {
                for (Enchantment enchantment : enchantments.keySet()) {
                        if (matches(enchantment) && enchantments.get(enchantment) >= minLevel) return true;
                }
                return false;
        }

        public boolean matches(ItemStack item) {
                return matches(item.getEnchantments());
        }

        @Override
        public String toString() {
                if (minLevel > 0) return enchantment.getName() + ":" + minLevel;
                return enchantment.getName();
        }
}
