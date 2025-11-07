package net.deadlydiamond98.koalalib.networking.packets.c2s;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.common.blocks.interaction.IHitBlockAction;
import net.deadlydiamond98.koalalib.common.entity.IHitEntityAction;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class PunchEntityC2SPacket {
    public static final Identifier ID = new Identifier(KoalaLib.MOD_ID, "punch_entity_packet");

    public static void send(boolean wasAttacking, Entity entity) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(wasAttacking);
        buf.writeInt(entity.getId());
        ClientPlayNetworking.send(ID, buf);
    }

    public static class Handler {
        public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            boolean wasAttacking = buf.readBoolean();
            int entityID = buf.readInt();
            server.execute(() -> {
                Entity entity = player.getWorld().getEntityById(entityID);

                if (entity instanceof IHitEntityAction hitEntity) {
                    hitEntity.attemptAttack(wasAttacking, entity, player.getWorld(), player);
                }
            });
        }
    }
}
