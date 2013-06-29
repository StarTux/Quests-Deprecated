
package com.winthier.quests.item;

import com.winthier.quests.util.Util;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.milkbowl.vault.item.ItemInfo;
import net.milkbowl.vault.item.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Potion
 * Potion:2696
 * Potion:2696:title=Walt's drink
 * Potion:2696|title=Walt's drink|lore=Walt walt walt
 */
public class ItemManager {
        private static Map<String, Material> nameMap = new HashMap<String, Material>();

        static {
                for (Material mat : Material.values()) {
                        //Util.logInfo("ItemManager: Registering material " + mat.name());
                        String name = mat.name().replaceAll("_", "");
                        if (nameMap.get(name) != null) {
                                System.err.println("ItemManager: duplicate name: " + name);
                        }
                        nameMap.put(name, mat);
                }
        }

        public static QuestItem fromString(String string) {
                String tokens[] = string.split(Pattern.quote("|"));
                if (tokens.length > 1) {
                        return complexItem(tokens);
                } else {
                        return simpleItem(tokens[0]);
                }
        }

        public static QuestItem fromStringList(List<String> list) {
                if (list.isEmpty()) return new AnyQuestItem();
                if (list.size() == 1) return fromString(list.get(0));
                MultipleItemMatcher result = new MultipleItemMatcher();
                for (String string : list) {
                        QuestItem item = fromString(string);
                        if (item == null) {
                                Util.logWarning("ItemManager: Invalid item: " + string);
                                continue;
                        }
                        result.add(item);
                }
                return result;
        }

        private static Material parseMaterial(String string) {
                try {
                        return Material.getMaterial(Integer.parseInt(string));
                } catch (NumberFormatException nfe) {}
                try {
                        return Material.valueOf(string.toUpperCase().replaceAll("-| ", "_"));
                } catch (IllegalArgumentException iae) {}
                return nameMap.get(string.toUpperCase().replaceAll("-|_", ""));
        }

        private static QuestItem simpleItem(String string) {
                String tokens[] = string.split(Pattern.quote(":"));
                if (tokens.length > 2) {
                        Util.logWarning("ItemManager: More than 2 fields: " + string);
                        return null;
                }
                Material mat = parseMaterial(tokens[0]);
                if (mat != null && tokens.length == 1) {
                        return new MaterialMatcher(mat);
                }
                int data = 0;
                if (mat == null) {
                        ItemInfo info = Items.itemByString(tokens[0]);
                        if (info != null) {
                                mat = info.getType();
                                data = (int)info.getSubTypeId();
                        }
                }
                if (mat == null) {
                        Util.logWarning("ItemManager: Invalid material: " + tokens[0]);
                        return null;
                }
                if (mat == Material.AIR) return new AirMatcher();
                if (tokens.length == 2) {
                        try {
                                data = Integer.parseInt(tokens[1]);
                        } catch (NumberFormatException nfe) {
                                Util.logWarning("ItemManager: Number expected: " + tokens[1]);
                                return null;
                        }
                }
                return new MaterialDataMatcher(mat, data);
        }

        private static QuestItem complexItem(String string[]) {
                QuestItem base = simpleItem(string[0]);
                if (base == null) return null;
                String s[] = new String[string.length - 1];
                System.arraycopy(string, 1, s, 0, s.length);
                return ComplexItemMatcher.create(base, s);
        }
}
