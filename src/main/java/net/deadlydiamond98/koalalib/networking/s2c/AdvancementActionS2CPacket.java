package net.deadlydiamond98.koalalib.networking.s2c;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.common.advancement.CustomAdvancement;
import net.deadlydiamond98.koalalib.common.entity.IHitEntityAction;
import net.deadlydiamond98.koalalib.networking.c2s.PunchEntityC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public record AdvancementActionS2CPacket(Identifier advancementId) implements CustomPayload {
    public static final Id<AdvancementActionS2CPacket> ID = new Id<>(Identifier.of(KoalaLib.MOD_ID, "advancement_action_client_packet"));
    public static final PacketCodec<PacketByteBuf, AdvancementActionS2CPacket> CODEC = PacketCodec.tuple(
            Identifier.PACKET_CODEC, AdvancementActionS2CPacket::advancementId,
            AdvancementActionS2CPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Sender {
        public static void send(ServerPlayerEntity player, Identifier advancementId) {
            ServerPlayNetworking.send(player, new AdvancementActionS2CPacket(advancementId));
        }
    }

    public static void receive(AdvancementActionS2CPacket payload, ClientPlayNetworking.Context context) {
        Identifier id = payload.advancementId();
        context.client().execute(() -> CustomAdvancement.CUSTOM_ADVANCEMENTS.get(id).doWhenTriggered(context.player()));
    }
}
