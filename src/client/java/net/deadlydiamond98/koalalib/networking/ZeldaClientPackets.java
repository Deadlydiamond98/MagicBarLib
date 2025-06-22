package net.deadlydiamond98.koalalib.networking;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.networking.packets.EntityStatsPacketS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class ZeldaClientPackets {
    public static final Identifier EntityStatsPacket = new Identifier(KoalaLib.MOD_ID, "entity_stats_packet");


    public static void registerC2SPackets() {
        ClientPlayNetworking.registerGlobalReceiver(EntityStatsPacket, EntityStatsPacketS2CPacket::recieve);
    }
}
