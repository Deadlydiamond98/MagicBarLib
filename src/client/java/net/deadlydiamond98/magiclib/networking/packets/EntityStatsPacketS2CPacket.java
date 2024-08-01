package net.deadlydiamond98.magiclib.networking.packets;

import net.deadlydiamond98.magiclib.util.ManaEntityData;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class EntityStatsPacketS2CPacket {
    public static void recieve(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf,
                               PacketSender responseSender) {
        int level = buf.readInt();
        int maxLevel = buf.readInt();
        int whenNeededRenderTime = buf.readInt();
        client.execute(() -> {
                if (client.player != null) {
                    client.player.setMana(level);
                    client.player.setMaxMana(maxLevel);
                    client.player.setWhenNeededRenderTime(whenNeededRenderTime);
                }
        });
    }
}
