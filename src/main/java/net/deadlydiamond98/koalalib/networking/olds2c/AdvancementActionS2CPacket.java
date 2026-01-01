package net.deadlydiamond98.koalalib.networking.olds2c;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.common.advancement.CustomAdvancement;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class AdvancementActionS2CPacket {
    public static final Identifier ID = new Identifier(KoalaLib.MOD_ID, "advancement_action_client_packet");

    public static void send(ServerPlayerEntity player, Identifier id) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeIdentifier(id);
        ServerPlayNetworking.send(player, ID, buf);
    }

    public static class Handler {
        public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            Identifier id = buf.readIdentifier();
            client.execute(() -> CustomAdvancement.CUSTOM_ADVANCEMENTS.get(id).doWhenTriggered(client.player));
        }
    }
}
