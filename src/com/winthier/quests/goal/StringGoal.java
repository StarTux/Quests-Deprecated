package com.winthier.quests.goal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class StringGoal extends QuotaGoal {
        private final List<Pattern> patterns = new ArrayList<Pattern>();

        @Override
        public void load(ConfigurationSection config) {
                super.load(config);
                for (String regex : config.getStringList("Strings")) {
                        Pattern pattern;
                        try {
                                pattern = Pattern.compile(regex);
                        } catch (PatternSyntaxException pse) {
                                quest.logWarning(config.getCurrentPath() + ".Strings: invalid regex: /" + regex + "/");
                                pse.printStackTrace();
                                continue;
                        }
                        patterns.add(pattern);
                }
        }

        public boolean feed(Player player, String string) {
                if (matches(string)) {
                        addScore(player, 1);
                        return true;
                }
                return false;
        }

        public boolean matches(String string) {
                for (Pattern pattern : patterns) {
                        Matcher matcher = pattern.matcher(string);
                        if (matcher.matches()) return true;
                }
                return false;
        }
}
