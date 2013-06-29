package com.winthier.quests.quest;

import com.winthier.quests.util.Util;
import java.util.ArrayList;
import java.util.List;
import net.milkbowl.vault.item.ItemInfo;
import net.milkbowl.vault.item.Items;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class CategoryInfo {
        private String name;
        private String displayName;
        private List<String> description;
        private ItemStack icon;

        public CategoryInfo() {}

        public String getName() {
                return name;
        }

        public String getDisplayName() {
                return displayName;
        }

        public List<String> getDescription() {
                return description;
        }

        public ItemStack getIcon() {
                return icon.clone();
        }

        public void load(ConfigurationSection config) {
                name = config.getString("Name");
                displayName = config.getString("DisplayName");
                description = new ArrayList<String>();
                for (String line : config.getStringList("Description")) {
                        description.addAll(Util.fillParagraph(line, 24));
                }
                ItemInfo info = Items.itemByString(config.getString("Icon", "1"));
                icon = info.toStack();
        }
}
