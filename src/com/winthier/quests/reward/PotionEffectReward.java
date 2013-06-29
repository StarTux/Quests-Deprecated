package com.winthier.quests.reward;

import com.winthier.quests.quest.Quest;
import com.winthier.quests.util.Util;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionEffectReward extends AbstractReward {
        private final List<PotionEffect> effects = new ArrayList<PotionEffect>();

        public PotionEffectReward(Quest quest) {
                super(quest);
        }

        @Override
        public void load(ConfigurationSection config) {
                super.load(config);
                for (String key : config.getKeys(false)) {
                        String tokens[] = key.split(":", 2);
                        PotionEffectType pet = PotionEffectType.getByName(tokens[0].toUpperCase().replaceAll("-", "_"));
                        if (pet == null) {
                                Util.logWarning("PotionEffectReward: " + config.getCurrentPath() + ": invalid potion effect: " + key);
                                continue;
                        }
                        int amplifier = 1;
                        if (tokens.length >= 2) {
                                try {
                                        amplifier = Integer.parseInt(tokens[1]);
                                } catch (NumberFormatException nfe) {
                                        nfe.printStackTrace();
                                }
                        }
                        int duration = config.getInt(key);
                        PotionEffect pe = new PotionEffect(pet, duration, amplifier);
                        effects.add(pe);
                }
        }

        @Override
        public void give(Player player) {
                for (PotionEffect pe : effects) player.addPotionEffect(pe);
        }

        private static String effectToString(PotionEffect effect) {
                return Util.camelCase("_", effect.getType().getName()) + ":" + effect.getAmplifier() + ":" + effect.getDuration();
        }

        @Override
        public String getDescription() {
                if (effects.isEmpty()) return null;
                StringBuilder sb = new StringBuilder(effectToString(effects.get(0)));
                for (int i = 1; i < effects.size(); ++i) {
                        sb.append(", ").append(effectToString(effects.get(i)));
                }
                return sb.toString();
        }
}
