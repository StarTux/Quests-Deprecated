package com.winthier.quests.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 * An interface for matching and creating entities.
y */
public class EntityTypeMatcher implements QuestEntity {
        private EntityType entityType;

        public EntityTypeMatcher(EntityType entityType) {
                this.entityType = entityType;
        }

        public boolean matches(Entity entity) {
                return entityType == entity.getType();
        }

        public boolean matches(EntityType entityType) {
                return this.entityType == entityType;
        }

        public Entity spawnEntity(Location location) {
                return location.getWorld().spawnEntity(location, entityType);
        }
}
