package net.deadlydiamond98.koalalib.networking.s2c;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.util.magic.MagicBarHelper;
import net.deadlydiamond98.koalalib.util.mixinterfaces.player.IPlayerOtherMixinData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public record EntityMagicUpdateS2CPacket(int level) implements CustomPayload {
    public static final Id<EntityMagicUpdateS2CPacket> ID = new Id<>(Identifier.of(KoalaLib.MOD_ID, "entity_magic_meter_packet"));
    public static final PacketCodec<PacketByteBuf, EntityMagicUpdateS2CPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, EntityMagicUpdateS2CPacket::level,
            EntityMagicUpdateS2CPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Sender {
        public static void send(ServerPlayerEntity player, int level) {
            ServerPlayNetworking.send(player, new EntityMagicUpdateS2CPacket(level));
        }
    }

    public static void receive(EntityMagicUpdateS2CPacket payload, ClientPlayNetworking.Context context) {
        int level = payload.level();
        context.client().execute(() -> {
            if (context.player() != null) {
                MagicBarHelper.setMana(context.player(), level);
                MagicBarHelper.getBar(context.player()).koalalib$setMagicBarRenderTime(100);
            }
        });
    }
}

