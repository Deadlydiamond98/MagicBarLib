package net.deadlydiamond98.koalalib.networking.packets.c2s;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.common.blocks.interaction.IHitBlockAction;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class PunchBlockC2SPacket {
    public static final Identifier ID = new Identifier(KoalaLib.MOD_ID, "punch_block_packet");

    public static void send(boolean wasAttacking, BlockPos blockPos) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(wasAttacking);
        buf.writeBlockPos(blockPos);
        ClientPlayNetworking.send(ID, buf);
    }

    public static class Handler {
        public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            boolean wasAttacking = buf.readBoolean();
            BlockPos blockPos = buf.readBlockPos();
            server.execute(() -> {
                BlockState state = player.getWorld().getBlockState(blockPos);
                if (state.getBlock() instanceof IHitBlockAction hitBlock) {
                    hitBlock.attemptAttack(wasAttacking, state, blockPos, player.getWorld(), player);
                }
            });
        }
    }
}
