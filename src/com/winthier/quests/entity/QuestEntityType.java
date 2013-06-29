package com.winthier.quests.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public enum QuestEntityType implements QuestEntity {
        SKELETON(EntityType.SKELETON),
        WITHER_SKELETON(EntityType.SKELETON),
        ;

        public final EntityType entityType;

        QuestEntityType(EntityType entityType) {
                this.entityType = entityType;
        }

        @Override
        public boolean matches(Entity entity) {
                return entity.getType() == entityType;
        }

        @Override
        public boolean matches(EntityType entityType) {
                return this.entityType = entityType;
        }        

        @Override
        public Entity spawnEntity(Location location) {
                return location.getWorld().spawnEntity(location, entityType);
        }
}
