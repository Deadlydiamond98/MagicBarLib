package net.deadlydiamond98.koalalib.networking;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class KoalaServerPackets {

    public static final Identifier EntityStatsPacket = new Identifier(KoalaLib.MOD_ID, "entity_stats_packet");

    public static void sendPlayerStatsPacket(ServerPlayerEntity player, int level, int maxLevel, int whenNeededRenderTime) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(level);
        buf.writeInt(maxLevel);
        buf.writeInt(whenNeededRenderTime);
        ServerPlayNetworking.send(player, EntityStatsPacket, buf);
    }
}
