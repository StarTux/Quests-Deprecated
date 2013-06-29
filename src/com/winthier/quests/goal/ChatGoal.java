package com.winthier.quests.goal;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ChatGoal extends StringGoal {
        public ChatGoal() {}

        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
                if (matches(event.getMessage())) {
                        new ChatTask(this, event).runTask(quest.plugin);
                }
        }

        public void onPlayerChat(AsyncPlayerChatEvent event) {
                Player player = event.getPlayer();
                if (!checkConstraints(player)) return;
                //feed(player, event.getMessage());
                addScore(player, 1);
        }
}


class ChatTask extends BukkitRunnable {
        private final ChatGoal goal;
        private final AsyncPlayerChatEvent event;

        ChatTask(ChatGoal goal, AsyncPlayerChatEvent event) {
                this.goal = goal;
                this.event = event;
        }

        @Override
        public void run() {
                goal.onPlayerChat(event);
        }
}
