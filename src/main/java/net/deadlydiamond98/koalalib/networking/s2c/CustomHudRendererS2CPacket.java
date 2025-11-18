package net.deadlydiamond98.koalalib.networking.s2c;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.util.mixindata.player.ICustomHudBarTextureMixinData;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class CustomHudRendererS2CPacket {
    public static final Identifier ID = new Identifier(KoalaLib.MOD_ID, "update_hud_render_packet");

    public static void send(ServerPlayerEntity player, Identifier texture, boolean showOutline, boolean canBlink, boolean isHealth) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeIdentifier(texture);
        buf.writeBoolean(showOutline);
        buf.writeBoolean(canBlink);
        buf.writeBoolean(isHealth);
        ServerPlayNetworking.send(player, ID, buf);
    }

    public static class Handler {
        public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            Identifier texture = buf.readIdentifier();

            boolean showOutline = buf.readBoolean();
            boolean canBlink = buf.readBoolean();

            boolean isHealth = buf.readBoolean();

            client.execute(() -> {
                if (client.player != null) {
                    if (isHealth) {
                        ((ICustomHudBarTextureMixinData) client.player).koalalib$applyCustomHeartTexture(texture, showOutline, canBlink);
                    } else {
                        ((ICustomHudBarTextureMixinData) client.player).koalalib$applyCustomShankTexture(texture, showOutline);
                    }
                }
            });
        }
    }
}
