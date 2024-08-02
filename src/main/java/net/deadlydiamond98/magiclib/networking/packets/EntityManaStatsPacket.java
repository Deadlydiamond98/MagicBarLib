package net.deadlydiamond98.magiclib.networking.packets;

import net.deadlydiamond98.magiclib.MagicLib;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record EntityManaStatsPacket(int level, int maxLevel, int whenNeededRenderTime) implements CustomPayload {
    public static final CustomPayload.Id<EntityManaStatsPacket> ID = new CustomPayload.Id<>(Identifier.of(MagicLib.MOD_ID, "entity_stats_packet"));
    public static final PacketCodec<PacketByteBuf, EntityManaStatsPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, EntityManaStatsPacket::level,
            PacketCodecs.INTEGER, EntityManaStatsPacket::maxLevel,
            PacketCodecs.INTEGER, EntityManaStatsPacket::whenNeededRenderTime,
            EntityManaStatsPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
