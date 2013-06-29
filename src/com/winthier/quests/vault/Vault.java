package com.winthier.quests.vault;

import com.winthier.quests.QuestsPlugin;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.item.Items;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Vault {
        private final QuestsPlugin plugin;
        public Permission permission;
        public Economy economy;
        public Items items;

        public Vault(QuestsPlugin plugin) {
                this.plugin = plugin;
        }

	public final void onEnable() {
		setupPermissions();
                setupEconomy();
                items = new Items();
	}

        public final void onDisable() {
                permission = null;
                economy = null;
                items = null;
        }

	private final boolean setupPermissions()
	{
		RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}

        private boolean setupEconomy()
        {
                RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
                if (economyProvider != null) {
                        economy = economyProvider.getProvider();
                }
                return (economy != null);
        }
}
