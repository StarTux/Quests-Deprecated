package com.winthier.quests.constraint;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public enum ConstraintType {
        ARMOR(ArmorConstraint.class),
        ITEM(ItemConstraint.class, "Items"),
        ITEM_IN_HAND(ItemInHandConstraint.class, "ItemInHand"),
        WORLD(WorldConstraint.class),
        WORLD_GUARD_REGION(WorldGuardRegionConstraint.class, "WGRegion", "WGRegions"),
        ;

        private static Map<String, ConstraintType> nameMap = new HashMap<String, ConstraintType>();
        private final Class<? extends Constraint> clazz;
        private final String names[];

        static {
                for (ConstraintType constraintType : values()) {
                        nameMap.put(constraintType.name().replaceAll("_|-", ""), constraintType);
                        for (String name : constraintType.names) {
                                nameMap.put(name.toUpperCase().replaceAll("_|-", ""), constraintType);
                        }
                }
        }

        ConstraintType(Class<? extends Constraint> clazz, String... names) {
                this.clazz = clazz;
                this.names = names;
        }

        public Constraint createConstraint() {
                Constraint result = null;
                try {
                        Constructor<? extends Constraint> ctor = clazz.getConstructor();
                        result = ctor.newInstance();
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return result;
        }

        public static ConstraintType matchString(String key) {
                return nameMap.get(key.toUpperCase().replaceAll("_|-", ""));
        }

        public static Constraint fromString(String key) {
                ConstraintType constraintType = matchString(key);
                if (constraintType == null) return null;
                return constraintType.createConstraint();
        }
}
