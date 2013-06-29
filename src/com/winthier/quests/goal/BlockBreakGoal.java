package com.winthier.quests.goal;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class BlockBreakGoal extends ItemGoal {
        private boolean mustBeNatural;
        private boolean mustDropExp;
        public static String notNaturalKey = "NotNatural";
        public static FixedMetadataValue notNaturalValue;
        private static NaturalBlockListener naturalBlockListener = null;

        public BlockBreakGoal() {}

        @Override
        public void enable() {
                super.enable();
                if (mustBeNatural && naturalBlockListener == null) {
                        notNaturalValue = new FixedMetadataValue(quest.plugin, true);
                        naturalBlockListener = new NaturalBlockListener();
                        quest.plugin.getServer().getPluginManager().registerEvents(naturalBlockListener, quest.plugin);
                }
        }

        @Override
        public void load(ConfigurationSection config) {
                super.load(config);
                mustDropExp = config.getBoolean("MustDropExp", false);
                mustBeNatural = config.getBoolean("MustBeNatural", false);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onBlockBreak(BlockBreakEvent event) {
                Player player = event.getPlayer();
                if (!checkConstraints(player)) return;
                if (mustDropExp && event.getExpToDrop() == 0) return;
                if (mustBeNatural && event.getBlock().hasMetadata(notNaturalKey)) return;
                feed(player, event.getBlock());
        }
}

class NaturalBlockListener implements Listener {
        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onBlockPlace(BlockPlaceEvent event) {
                event.getBlock().setMetadata(BlockBreakGoal.notNaturalKey, BlockBreakGoal.notNaturalValue);
        }
}
