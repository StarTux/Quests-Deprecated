package com.winthier.quests.constraint;

import com.winthier.quests.item.ItemManager;
import com.winthier.quests.item.QuestItem;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ItemInHandConstraint extends AbstractConstraint {
        private QuestItem items;

        public ItemInHandConstraint() {}

        @Override
        public void load(ConfigurationSection config) {
                items = ItemManager.fromStringList(config.getStringList("Items"));
        }

        @Override
        public boolean isSatisfied(Player player) {
                return items.matches(player.getItemInHand());
        }
}
