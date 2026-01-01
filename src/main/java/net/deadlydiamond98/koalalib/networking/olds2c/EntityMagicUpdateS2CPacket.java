package net.deadlydiamond98.koalalib.networking.olds2c;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.util.magic.MagicBarHelper;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class EntityMagicUpdateS2CPacket {
    public static final Identifier ID = new Identifier(KoalaLib.MOD_ID, "entity_magic_meter_packet");

    public static void send(ServerPlayerEntity player, int level) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(level);
        ServerPlayNetworking.send(player, ID, buf);
    }

    public static class Handler {
        public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            int level = buf.readVarInt();
            client.execute(() -> {
                if (client.player != null) {
                    MagicBarHelper.setMana(client.player, level);
                    MagicBarHelper.getBar(client.player).koalalib$setMagicBarRenderTime(100);
                }
            });
        }
    }
}
