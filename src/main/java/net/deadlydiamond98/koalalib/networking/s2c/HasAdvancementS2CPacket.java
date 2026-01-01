package net.deadlydiamond98.koalalib.networking.s2c;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.util.mixinterfaces.player.IPlayerOtherMixinData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public record HasAdvancementS2CPacket(boolean hasAdvancement) implements CustomPayload {
    public static final Id<HasAdvancementS2CPacket> ID = new Id<>(Identifier.of(KoalaLib.MOD_ID, "check_for_advancement_packet"));
    public static final PacketCodec<PacketByteBuf, HasAdvancementS2CPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, HasAdvancementS2CPacket::hasAdvancement,
            HasAdvancementS2CPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Sender {
        public static void send(ServerPlayerEntity player, boolean hasAdvancement) {
            ServerPlayNetworking.send(player, new HasAdvancementS2CPacket(hasAdvancement));
        }
    }

    public static void receive(HasAdvancementS2CPacket payload, ClientPlayNetworking.Context context) {
        boolean hasAdvancement = payload.hasAdvancement();
        context.client().execute(() -> {
            if (context.player() != null) {
                ((IPlayerOtherMixinData) context.player()).koalalib$updateAdvancementClient(hasAdvancement);
            }
        });
    }
}

