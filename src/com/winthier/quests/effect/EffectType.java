package com.winthier.quests.effect;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public enum EffectType {
        ADVICE_ANIMAL(AdviceAnimalEffect.class),
        DELIVERY_CHEST(DeliveryChestEffect.class, "Delivery", "ItemDelivery", "DeliveryItem"),
        ;

        private static Map<String, EffectType> nameMap = new HashMap<String, EffectType>();
        private final Class<? extends Effect> clazz;
        private final String names[];

        static {
                for (EffectType effectType : values()) {
                        nameMap.put(effectType.name().replaceAll("_|-", ""), effectType);
                        for (String name : effectType.names) {
                                nameMap.put(name.toUpperCase(), effectType);
                        }
                }
        }

        EffectType(Class<? extends Effect> clazz, String... names) {
                this.clazz = clazz;
                this.names = names;
        }

        public Effect createEffect() {
                Effect result = null;
                try {
                        Constructor<? extends Effect> ctor = clazz.getConstructor();
                        result = ctor.newInstance();
                } catch (Throwable t) {
                        t.printStackTrace();
                }
                return result;
        }

        public static EffectType matchString(String name) {
                return nameMap.get(name.toUpperCase().replaceAll("_|-", ""));
        }

        public static Effect fromString(String string) {
                EffectType effectType = matchString(string);
                if (effectType == null) return null;
                return effectType.createEffect();
        }
}
