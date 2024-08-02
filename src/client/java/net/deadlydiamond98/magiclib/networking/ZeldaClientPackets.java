package net.deadlydiamond98.magiclib.networking;

import net.deadlydiamond98.magiclib.MagicLib;
import net.deadlydiamond98.magiclib.networking.packets.EntityManaStatsPacket;
import net.deadlydiamond98.magiclib.networking.packets.EntityStatsPacketS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class ZeldaClientPackets {
//    public static final Identifier EntityStatsPacket = new Identifier(MagicLib.MOD_ID, "entity_stats_packet");


    public static void registerC2SPackets() {
        ClientPlayNetworking.registerGlobalReceiver(EntityManaStatsPacket.ID, EntityStatsPacketS2CPacket::recieve);
    }
}
