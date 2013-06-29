package com.winthier.quests.protocol;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.winthier.quests.QuestsPlugin;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Protocol {
        private final QuestsPlugin plugin;
        private ProtocolManager protocolManager;

        public Protocol(QuestsPlugin plugin) {
                this.plugin = plugin;
        }

        public void onEnable() {
                protocolManager = ProtocolLibrary.getProtocolManager();
        }

        public void onDisable() {
                protocolManager = null;
        }

        public void playSound(Player player, String soundName, Location location) {
                playSound(player, soundName, location, 1.0f);
        }

        public void playSound(Player player, String soundName, Location location, float volume) {
                playSound(player, soundName, location, volume, 63);
        }

        public void playSound(Player player, String soundName, Location location, float volume, int pitch) {
                PacketContainer packet = protocolManager.createPacket(Packets.Server.NAMED_SOUND_EFFECT);
                packet.getStrings().write(0, soundName);
                packet.getIntegers()
                        .write(0, location.getBlockX() * 8)
                        .write(1, location.getBlockY() * 8)
                        .write(2, location.getBlockZ() * 8);
                packet.getFloat().write(0, volume);
                packet.getIntegers().write(3, pitch);
                try {
                        protocolManager.sendServerPacket(player, packet);
                } catch (InvocationTargetException ite) {
                        ite.printStackTrace();
                }
        }
}
