package net.deadlydiamond98.koalalib.networking;

import net.deadlydiamond98.koalalib.networking.packets.EntityMagicStatsS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class KoalaPackets {

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(EntityMagicStatsS2CPacket.ID, EntityMagicStatsS2CPacket::recieve);
    }

    public static void registerC2SPackets() {
    }
}
