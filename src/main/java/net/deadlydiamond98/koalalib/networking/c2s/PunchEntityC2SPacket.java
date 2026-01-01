package net.deadlydiamond98.koalalib.networking.c2s;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.common.entity.IHitEntityAction;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record PunchEntityC2SPacket(boolean wasAttacking, int entityID) implements CustomPayload {
    public static final Id<PunchEntityC2SPacket> ID = new Id<>(Identifier.of(KoalaLib.MOD_ID, "punch_entity_packet"));

    public static final PacketCodec<PacketByteBuf, PunchEntityC2SPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, PunchEntityC2SPacket::wasAttacking,
            PacketCodecs.INTEGER, PunchEntityC2SPacket::entityID,
            PunchEntityC2SPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Sender {
        public static void send(boolean wasAttacking, Entity entity) {
            ClientPlayNetworking.send(new PunchEntityC2SPacket(wasAttacking, entity.getId()));
        }
    }

    public static void receive(PunchEntityC2SPacket payload, ServerPlayNetworking.Context context) {
        boolean wasAttacking = payload.wasAttacking();
        int entityID = payload.entityID();

        context.server().execute(() -> {
            Entity entity = context.player().getWorld().getEntityById(entityID);

            if (entity instanceof IHitEntityAction hitEntity) {
                hitEntity.attemptAttack(wasAttacking, entity, context.player().getWorld(), context.player());
            }
        });
    }
}
