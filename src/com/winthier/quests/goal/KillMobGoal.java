package com.winthier.quests.goal;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class KillMobGoal extends EntityGoal {
        public KillMobGoal() {}

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onEntityDeath(EntityDeathEvent event) {
                LivingEntity entity = event.getEntity();
                EntityDamageEvent lastDamage = entity.getLastDamageCause();
                if (!(lastDamage instanceof EntityDamageByEntityEvent)) return;
                Entity damager = ((EntityDamageByEntityEvent)lastDamage).getDamager();
                Player player = null;;
                if (damager instanceof Player) {
                        player = (Player)damager;
                } else if (damager instanceof Projectile) {
                        Projectile projectile = (Projectile)damager;
                        if (projectile.getShooter() instanceof Player) {
                                player = (Player)projectile.getShooter();
                        }
                }
                if (player == null) return;
                if (!checkConstraints(player)) return;
                feed(player, entity);
        }
}
