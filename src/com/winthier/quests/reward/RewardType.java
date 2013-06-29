package com.winthier.quests.reward;

import com.winthier.quests.quest.Quest;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public enum RewardType {
        CATEGORY(CategoryReward.class),
        EXP(ExpReward.class, "xp", "experience"),
        EXP_LEVEL(ExpLevelReward.class, "xplevel"),
        FOOD(FoodReward.class, "hunger"),
        ITEM(ItemReward.class, "items"),
        MONEY(MoneyReward.class),
        POTION_EFFECT(PotionEffectReward.class, "effect"),
        RANK(RankReward.class),
        REWARDS(Rewards.class),
        ;

        private static Map<String, RewardType> nameMap = new HashMap<String, RewardType>();
        private final Class<? extends Reward> clazz;
        private final String names[];

        static {
                for (RewardType rewardType : values()) {
                        nameMap.put(rewardType.name().replaceAll("_|-", ""), rewardType);
                        for (String name : rewardType.names) {
                                nameMap.put(name.toUpperCase().replaceAll("_|-", ""), rewardType);
                        }
                }
        }

        RewardType(Class<? extends Reward> clazz, String... names) {
                this.clazz = clazz;
                this.names = names;
        }

        public Reward createReward(Quest quest) {
                Reward result = null;
                try {
                        Constructor<? extends Reward> ctor = clazz.getConstructor(Quest.class);
                        result = ctor.newInstance(quest);
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return result;
        }

        public static RewardType matchString(String key) {
                return nameMap.get(key.toUpperCase().replaceAll("_|-", ""));
        }

        public static Reward fromString(Quest quest, String key) {
                RewardType rewardType = matchString(key);
                if (rewardType == null) return null;
                return rewardType.createReward(quest);
        }
}
