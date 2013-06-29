package com.winthier.quests.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.bukkit.entity.EntityType;

public class EntityManager {
        private static Map<String, EntityType> nameMap = new HashMap<String, EntityType>();

        static {
                for (EntityType entityType : EntityType.values()) {
                        nameMap.put(entityType.name().replaceAll("_", ""), entityType);
                }
        }

        public static EntityType parseEntityType(String string) {
                return nameMap.get(string);
        }

        public static QuestEntity fromString(String string) {
                String tokens[] = string.split(Pattern.quote("|"));
                if (tokens.length == 1) {
                        return simpleEntity(tokens[0]);
                }
                return null;
        }

        public static EntityTypeMatcher simpleEntity(String string) {
                EntityType entityType = parseEntityType(string);
                if (entityType == null) return null;
                return new EntityTypeMatcher(entityType);
        }
}
