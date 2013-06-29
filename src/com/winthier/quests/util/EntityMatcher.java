package com.winthier.quests.util;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Skeleton;

public class EntityMatcher {
        private final EntityType entityType;
        private Skeleton.SkeletonType skeletonType;
        private Ocelot.Type catType;
        private int age = 0;
        private final static int BABY = 1;
        private final static int ADULT = 2;

        public EntityMatcher(EntityType entityType) {
                this.entityType = entityType;
        }

        public EntityMatcher(String string) throws IllegalArgumentException {
                String tokens[] = string.split(":");
                String token = tokens[0].toLowerCase().replaceAll("-|_", "");;
                if (token.equals("skeleton") || token.equals("skelly")) {
                        entityType = EntityType.SKELETON;
                        skeletonType = Skeleton.SkeletonType.NORMAL;
                } else if (token.equals("witherskeleton") || token.equals("witherskelly")) {
                        entityType = EntityType.SKELETON;
                        skeletonType = Skeleton.SkeletonType.WITHER;
                } else if (token.equals("ocelot")) {
                        entityType = EntityType.OCELOT;
                        catType = Ocelot.Type.WILD_OCELOT;
                } else if (token.equals("blackcat") || token.equals("tuxedocat")) {
                        entityType = EntityType.OCELOT;
                        catType = Ocelot.Type.BLACK_CAT;
                } else if (token.equals("redcat") || token.equals("tabbycat")) {
                        entityType = EntityType.OCELOT;
                        catType = Ocelot.Type.RED_CAT;
                } else if (token.equals("siamesecat")) {
                        entityType = EntityType.OCELOT;
                        catType = Ocelot.Type.SIAMESE_CAT;
                } else {
                        entityType = EntityType.valueOf(tokens[0].toUpperCase());
                }
                for (int i = 1; i < tokens.length; ++i) {
                        token = tokens[i].toLowerCase().replaceAll("-|_", "");
                        if (token.equals("baby")) {
                                age = BABY;
                        } else if (token.equals("adult")) {
                                age = ADULT;
                        } else {
                                Util.logWarning("[EntityMatcher]: invalid token: " + tokens[i]);
                        }
                }
        }

        public boolean matches(Entity entity) {
                if (entity.getType() != entityType) return false;
                if (entity instanceof Skeleton) {
                        Skeleton skeleton = (Skeleton)entity;
                        if (skeletonType != null && skeleton.getSkeletonType() != skeletonType) return false;
                }
                if (entity instanceof Ocelot) {
                        Ocelot ocelot = (Ocelot)entity;
                        if (catType != null && ocelot.getCatType() != catType) return false;
                }
                if (entity instanceof Ageable) {
                        Ageable ageable = (Ageable)entity;
                        if (age == BABY && ageable.isAdult()) return false;
                        if (age == ADULT && !ageable.isAdult()) return false;
                }
                return true;
        }

        public static EntityMatcher fromString(String string) {
                try {
                        return new EntityMatcher(string);
                } catch (IllegalArgumentException iae) {
                        return null;
                }
        }
}
