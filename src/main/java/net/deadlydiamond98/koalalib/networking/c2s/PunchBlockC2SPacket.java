package net.deadlydiamond98.koalalib.networking.c2s;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.common.blocks.interaction.IHitBlockAction;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record PunchBlockC2SPacket(boolean wasAttacking, BlockPos blockPos) implements CustomPayload {
    public static final Id<PunchBlockC2SPacket> ID = new Id<>(Identifier.of(KoalaLib.MOD_ID, "punch_block_packet"));

    public static final PacketCodec<PacketByteBuf, PunchBlockC2SPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, PunchBlockC2SPacket::wasAttacking,
            BlockPos.PACKET_CODEC, PunchBlockC2SPacket::blockPos,
            PunchBlockC2SPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Sender {
        public static void send(boolean wasAttacking, BlockPos pos) {
            ClientPlayNetworking.send(new PunchBlockC2SPacket(wasAttacking, pos));
        }
    }

    public static void receive(PunchBlockC2SPacket payload, ServerPlayNetworking.Context context) {
        boolean wasAttacking = payload.wasAttacking();
        BlockPos blockPos = payload.blockPos();

        context.server().execute(() -> {
            BlockState state = context.player().getWorld().getBlockState(blockPos);
            if (state.getBlock() instanceof IHitBlockAction hitBlock) {
                hitBlock.attemptAttack(wasAttacking, state, blockPos, context.player().getWorld(), context.player());
            }
        });
    }
}
