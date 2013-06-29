package com.winthier.quests.constraint;

import com.winthier.quests.item.ItemManager;
import com.winthier.quests.item.QuestItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

/**
 * Configuration:
 * armor:
 *   Helmet: [ Wool ]
 *   ChestPlate: [ Diamond-Chestplate ]
 *   Leggings: [ Gold-Leggings ]
 *   Boots: [ Air ]
 */
public class ArmorConstraint extends AbstractConstraint {
        private QuestItem helmet, chestplate, leggings, boots;

        public ArmorConstraint() {}

        @Override
        public void load(ConfigurationSection config) {
                super.load(config);
                helmet = ItemManager.fromStringList(config.getStringList("Helmet"));
                chestplate = ItemManager.fromStringList(config.getStringList("Chestplate"));
                leggings = ItemManager.fromStringList(config.getStringList("Leggings"));
                boots = ItemManager.fromStringList(config.getStringList("Boots"));
        }

        @Override
        public boolean isSatisfied(Player player) {
                PlayerInventory inv = player.getInventory();
                return
                        helmet.matches(inv.getHelmet()) &&
                        chestplate.matches(inv.getChestplate()) &&
                        leggings.matches(inv.getLeggings()) &&
                        boots.matches(inv.getBoots());
        }
}
