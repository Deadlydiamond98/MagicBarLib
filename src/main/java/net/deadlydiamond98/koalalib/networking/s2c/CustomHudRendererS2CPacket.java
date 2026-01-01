package net.deadlydiamond98.koalalib.networking.s2c;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.util.magic.MagicBarHelper;
import net.deadlydiamond98.koalalib.util.mixinterfaces.player.ICustomHudBarTextureMixinData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public record CustomHudRendererS2CPacket(Identifier texture, boolean showOutline, boolean canBlink, boolean isHealth) implements CustomPayload {
    public static final Id<CustomHudRendererS2CPacket> ID = new Id<>(Identifier.of(KoalaLib.MOD_ID, "update_hud_render_packet"));
    public static final PacketCodec<PacketByteBuf, CustomHudRendererS2CPacket> CODEC = PacketCodec.tuple(
            Identifier.PACKET_CODEC, CustomHudRendererS2CPacket::texture,
            PacketCodecs.BOOL, CustomHudRendererS2CPacket::showOutline,
            PacketCodecs.BOOL, CustomHudRendererS2CPacket::canBlink,
            PacketCodecs.BOOL, CustomHudRendererS2CPacket::isHealth,
            CustomHudRendererS2CPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Sender {
        public static void send(ServerPlayerEntity player, Identifier texture, boolean showOutline, boolean canBlink, boolean isHealth) {
            ServerPlayNetworking.send(player, new CustomHudRendererS2CPacket(texture, showOutline, canBlink, isHealth));
        }
    }

    public static void receive(CustomHudRendererS2CPacket payload, ClientPlayNetworking.Context context) {
        Identifier texture = payload.texture();
        boolean showOutline = payload.showOutline();
        boolean canBlink = payload.canBlink();
        boolean isHealth = payload.isHealth();

        context.client().execute(() -> {
            if (context.player() != null) {
                if (isHealth) {
                    ((ICustomHudBarTextureMixinData) context.player()).koalalib$applyCustomHeartTexture(texture, showOutline, canBlink);
                } else {
                    ((ICustomHudBarTextureMixinData) context.player()).koalalib$applyCustomShankTexture(texture, showOutline);
                }
            }
        });
    }
}

