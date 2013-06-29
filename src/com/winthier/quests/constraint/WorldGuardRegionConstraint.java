package com.winthier.quests.constraint;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * This constraint is only satisfied if the player is in any of
 * the whitelisted regions and in none of the blacklisted ones.
 * If the whitelist is empty, not being blacklisted is sufficient.
 *
 * Configuration structure:
 * constraints:
 *   wgregion: # Contains a mapping of worlds
 *     spawn: true # whitelisted
 *     resource: false # blacklisted
 */
public class WorldGuardRegionConstraint extends AbstractConstraint {
        private final List<String> whitelist = new ArrayList<String>();
        private final List<String> blacklist = new ArrayList<String>();

        public WorldGuardRegionConstraint() {}
        @Override
        public void load(ConfigurationSection config) {
                super.load(config);
                for (String key : config.getKeys(false)) {
                        if (config.getBoolean(key)) {
                                whitelist.add(key);
                        } else {
                                blacklist.add(key);
                        }
                }
        }

        public boolean isSatisfied(Player player) {
                World world = player.getWorld();
                RegionManager rm = WGBukkit.getRegionManager(world);
                boolean white = whitelist.isEmpty();
                for (ProtectedRegion pr : rm.getApplicableRegions(player.getLocation())) {
                        String id = pr.getId();
                        if (whitelist.contains(id)) white = true;
                        if (blacklist.contains(id)) return false;
                }
                return white;
        }
}
