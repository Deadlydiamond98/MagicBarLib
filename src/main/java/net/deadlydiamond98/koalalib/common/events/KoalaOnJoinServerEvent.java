package net.deadlydiamond98.koalalib.common.events;

import net.deadlydiamond98.koalalib.networking.olds2c.EntityMagicUpdateS2CPacket;
import net.deadlydiamond98.koalalib.util.magic.MagicBarHelper;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class KoalaOnJoinServerEvent {
    public static void register() {
        ServerPlayConnectionEvents.JOIN.register(KoalaOnJoinServerEvent::syncMana);
    }

    private static void syncMana(ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer server) {
        // Sends Packet to player on join to sync their magic meter with the client
        ServerPlayerEntity player = serverPlayNetworkHandler.player;
        EntityMagicUpdateS2CPacket.send(player, MagicBarHelper.getMana(player));
    }
}
