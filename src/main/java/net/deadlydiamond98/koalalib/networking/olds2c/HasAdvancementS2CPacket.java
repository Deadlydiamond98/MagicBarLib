package net.deadlydiamond98.koalalib.networking.olds2c;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.util.mixinterfaces.player.IPlayerOtherMixinData;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class HasAdvancementS2CPacket {
    public static final Identifier ID = new Identifier(KoalaLib.MOD_ID, "check_for_advancement_packet");

    public static void send(ServerPlayerEntity player, boolean hasAdvancement) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(hasAdvancement);
        ServerPlayNetworking.send(player, ID, buf);
    }

    public static class Handler {
        public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            boolean hasAdvancement = buf.readBoolean();
            client.execute(() -> {
                if (client.player != null) {
                    ((IPlayerOtherMixinData) client.player).koalalib$updateAdvancementClient(hasAdvancement);
                }
            });
        }
    }
}
