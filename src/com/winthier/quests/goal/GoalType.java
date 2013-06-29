package com.winthier.quests.goal;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public enum GoalType {
        ADVICE_ANIMAL(AdviceAnimalGoal.class, "AdviceAnimals"),
        BED_LEAVE(BedLeaveGoal.class, "Sleep", "Sleeping", "LeaveBed"),
        BLOCK_BREAK(BlockBreakGoal.class, "BreakBlock"),
        BLOCK_INTERACT(BlockInteractGoal.class, "InteractBlock"),
        BLOCK_PLACE(BlockPlaceGoal.class, "PlaceBlock", "PlaceBlocks"),
        BUCKET_FILL(BucketFillGoal.class, "FillBucket"),
        CHAT(ChatGoal.class),
        COMMAND(CommandGoal.class),
        CRAFT_ITEM(CraftItemGoal.class, "ItemCraft", "Crafting"),
        DELIVER_ITEM(DeliverItemGoal.class, "ItemDeliver", "ItemDelivery", "DeliverItems"),
        DUMMY(DummyGoal.class),
        ENCHANT_ITEM(EnchantItemGoal.class, "Enchant", "ItemEnchant"),
        FISH(FishGoal.class, "Fishing"),
        FURNACE_EXTRACT(FurnaceExtractGoal.class, "Smelting", "SmeltItem", "Smelt", "Cook", "Cooking"),
        ITEM_CONSUME(ItemConsumeGoal.class, "ConsumeItem", "Consume", "ItemEat", "EatItem", "Eat", "Eating", "Drink", "Drinking"),
        KILL_MOB(KillMobGoal.class, "MobKill", "KillMobs"),
        MOB_ARENA(MobArenaGoal.class),
        SIMPLE_SHOP(SimpleShopGoal.class),
        ;

        private static Map<String, GoalType> nameMap = new HashMap<String, GoalType>();
        private final Class<? extends Goal> clazz;
        private final String names[];

        static {
                for (GoalType goalType : values()) {
                        nameMap.put(goalType.name().replaceAll("_|-", ""), goalType);
                        for (String name : goalType.names) {
                                nameMap.put(name.toUpperCase(), goalType);
                        }
                }
        }

        GoalType(Class<? extends Goal> clazz, String... names) {
                this.clazz = clazz;
                this.names = names;
        }

        public Goal createGoal() {
                Goal result = null;
                try {
                        Constructor<? extends Goal> ctor = clazz.getConstructor();
                        result = ctor.newInstance();
                } catch (Throwable t) {
                        t.printStackTrace();
                }
                return result;
        }

        public static GoalType matchString(String name) {
                return nameMap.get(name.toUpperCase().replaceAll("_|-", ""));
        }

        public static Goal fromString(String string) {
                GoalType goalType = matchString(string);
                if (goalType == null) return null;
                return goalType.createGoal();
        }
}
