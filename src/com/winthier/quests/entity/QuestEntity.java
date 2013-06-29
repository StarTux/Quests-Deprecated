package com.winthier.quests.entity;

import org.bukkit.Location;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 * An interface for matching and creating entities.
 */
public interface QuestEntity {
        public boolean matches(Entity entity);
        public boolean matches(EntityType entityType);
        public Entity spawnEntity(Location location);
}
