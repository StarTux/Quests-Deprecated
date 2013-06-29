package com.winthier.quests.event;

import com.winthier.adviceanimals.AdviceAnimal;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

public class DeliveryChestEvent extends Event {
        private static HandlerList handlers = new HandlerList();
        private final Player player;
        private final AdviceAnimal animal;
        private final Inventory inventory;

        public DeliveryChestEvent(Player player, AdviceAnimal animal, Inventory inventory) {
                this.player = player;
                this.animal = animal;
                this.inventory = inventory;
        }

        public Player getPlayer() {
                return player;
        }

        public AdviceAnimal getAdviceAnimal() {
                return animal;
        }

        public Inventory getInventory() {
                return inventory;
        }

        public static HandlerList getHandlerList() {
                return handlers;
        }

        @Override
        public HandlerList getHandlers() {
                return handlers;
        }
}
