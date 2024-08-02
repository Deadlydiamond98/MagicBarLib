package net.deadlydiamond98.magiclib.networking;

import net.deadlydiamond98.magiclib.networking.packets.EntityManaStatsPacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class MagicServerPackets {

    public static void registerServerPackets() {
        PayloadTypeRegistry.playS2C().register(EntityManaStatsPacket.ID, EntityManaStatsPacket.CODEC);
    }

    public static void sendPlayerStatsPacket(ServerPlayerEntity player, int level, int maxLevel, int whenNeededRenderTime) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(level);
        buf.writeInt(maxLevel);
        buf.writeInt(whenNeededRenderTime);
        ServerPlayNetworking.send(player, new EntityManaStatsPacket(level, maxLevel, whenNeededRenderTime));
    }
}
