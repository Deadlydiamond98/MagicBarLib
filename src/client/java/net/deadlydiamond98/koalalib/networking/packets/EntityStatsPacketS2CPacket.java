package net.deadlydiamond98.koalalib.networking.packets;

import net.deadlydiamond98.koalalib.util.magic.MagicBarHelper;
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
                    MagicBarHelper.setMana(client.player, level);
                    MagicBarHelper.setMaxMana(client.player, maxLevel);
                    MagicBarHelper.getBar(client.player).koalalib$setMagicBarRenderTime(whenNeededRenderTime);
                }
        });
    }
}
