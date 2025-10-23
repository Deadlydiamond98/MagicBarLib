package net.deadlydiamond98.koalalib.networking.packets.s2c;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.util.mixindata.ICustomGlowingMixinData;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class EntityGlowColorS2CPacket {
    public static final Identifier ID = new Identifier(KoalaLib.MOD_ID, "entity_glow_packet");

    public static void send(ServerPlayerEntity player, int entityID, Optional<Integer> hex) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(entityID);
        buf.writeOptional(hex, PacketByteBuf::writeInt);
        ServerPlayNetworking.send(player, ID, buf);
    }

    public static class Handler {
        public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            int id = buf.readInt();
            Optional<Integer> hex = buf.readOptional(PacketByteBuf::readInt);

            client.execute(() -> {
                if (client.player != null && client.player.getWorld().getEntityById(id) instanceof ICustomGlowingMixinData entity) {
                    entity.koalalib$setGlowColor(hex);
                }
            });
        }
    }
}
