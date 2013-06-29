package com.winthier.quests.util;

import com.winthier.adviceanimals.AdviceAnimalsPlugin;
import com.winthier.quests.QuestsPlugin;
import com.winthier.quests.item.ItemManager;
import com.winthier.quests.item.QuestItem;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Util {
        public static List<String> getStringList(Object o) {
                List<String> result;
                if (o == null) {
                        result = new ArrayList<String>(0);
                        return result;
                }
                if (o instanceof String) {
                        result = new ArrayList<String>(1);
                        result.add((String)o);
                        return result;
                }
                if (o instanceof List) {
                        List<?> l = (List<?>)o;
                        result = new ArrayList<String>(l.size());
                        for (Object ol : l) {
                                result.add(ol.toString());
                        }
                        return result;
                }
                result = new ArrayList<String>(1);
                result.add(o.toString());
                return result;
        }

        public static String listToString(List<String> list) {
                if (list.isEmpty()) return "";
                StringBuilder sb = new StringBuilder(list.get(0));
                for (int i = 0; i < list.size(); ++i) {
                        sb.append(" ").append(list.get(i));
                }
                return sb.toString();
        }

        public static int getInt(Object o, final int deafult) {
                if (o == null) return deafult;
                if (o instanceof String) {
                        try {
                                return Integer.parseInt((String)o);
                        } catch (NumberFormatException nfe) {
                                return deafult;
                        }
                }
                if (o instanceof Number) {
                        return ((Number)o).intValue();
                }
                return deafult;
        }

        public static int getInt(Object o) {
                return getInt(o, 0);
        }

        public static List<ConfigurationSection> getSectionList(List<Map<?, ?>> maps) {
                List<ConfigurationSection> result;
                if (maps == null) {
                        result = new ArrayList<ConfigurationSection>(0);
                        return result;
                }
                result = new ArrayList<ConfigurationSection>(maps.size());
                for (Map<?, ?> map : maps) {
                        ConfigurationSection section = new MemoryConfiguration();
                        for (Object o : map.keySet()) {
                                section.set(o.toString(), map.get(o));
                        }
                        result.add(section);
                }
                return result;
        }

        /**
         * Copied straight from org.bukkit.configuration.file.YamlConfiguration
         * Where there it is protected for some reason
         */
        public static void convertMapsToSections(Map<?, ?> input, ConfigurationSection section) {
                for (Map.Entry<?, ?> entry : input.entrySet()) {
                        String key = entry.getKey().toString();
                        Object value = entry.getValue();
                        if (value instanceof Map) {
                                convertMapsToSections((Map<?, ?>) value, section.createSection(key));
                        } else {
                                section.set(key, value);
                        }
                }
        }


        public static void print(Player player, String message) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }

        public static void copyResource(QuestsPlugin plugin, String filename, boolean force) {
                copyResource(plugin, filename, filename, force);
        }

        public static void copyResource(QuestsPlugin plugin, String filename, String dest, boolean force) {
                File destFile = new File(plugin.getDataFolder(), dest);
                if (!force && destFile.exists()) return;
                InputStream in = plugin.getResource(filename);
                if (in == null) return;
                FileOutputStream out = null;
                try {
                        destFile.getParentFile().mkdirs();
                        out = new FileOutputStream(destFile);
                        int b;
                        while (-1 != (b = in.read())) {
                                out.write(b);
                        }
                } catch (IOException ioe) {
                        ioe.printStackTrace();
                        return;
                } finally {
                        try {
                                in.close();
                                if (out != null) out.close();
                        } catch (IOException ioe) {}
                }
        }

        public static void logInfo(String msg) {
                System.out.println("[Quests] " + msg);
        }

        public static void logWarning(String msg) {
                System.err.println("[Quests] " + msg);
        }

        public static Location parseLocation(String string) {
                String tokens[] = string.split(":|,", 4);
                if (tokens[0].equalsIgnoreCase("#AdviceAnimal")) {
                        if (tokens.length != 2) return null;
                        return AdviceAnimalsPlugin.getInstance().getAnimal(tokens[1]).getLocation();
                }
                if (tokens.length != 4) return null;
                World world = Bukkit.getServer().getWorld(tokens[0]);
                if (world == null) return null;
                double coords[] = new double[3];
                try {
                        for (int i = 0; i < 3; ++i) {
                                coords[i] = Double.parseDouble(tokens[i + 1]);
                        }
                } catch (NumberFormatException nfe) {
                        return null;
                }
                return new Location(world, coords[0], coords[1], coords[2]);
        }

        public static ItemStack parseItemStack(String string) {
                QuestItem item = ItemManager.fromString(string);
                if (item == null) return null;
                return item.toItemStack();
        }

        public static List<String> fillParagraph(String par, int width) {
                List<String> result = new ArrayList<String>();
                StringBuilder line = new StringBuilder();
                for (String word : par.split(" +")) {
                        if (line.length() + ChatColor.stripColor(word).length() + 1 > width) {
                                result.add(line.toString());
                                line = new StringBuilder(word);
                        } else {
                                if (line.length() > 0) line.append(" ");
                                line.append(word);
                        }
                }
                if (line.length() > 0) result.add(line.toString());
                return result;
        }

        public static String capitalize(String string) {
                if (string.length() == 0) return "";
                if (string.length() == 1) return string.toUpperCase();
                return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
        }

        public static String camelCase(String delim, String string) {
                String tokens[] = string.split(delim);
                StringBuilder sb = new StringBuilder(capitalize(tokens[0]));
                for (int i = 1; i < tokens.length; ++i) sb.append(capitalize(tokens[i]));
                return sb.toString();
        }
}
