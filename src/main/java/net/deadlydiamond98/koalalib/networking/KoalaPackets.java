package net.deadlydiamond98.koalalib.networking;

import net.deadlydiamond98.koalalib.networking.packets.c2s.LeftClickItemC2SPacket;
import net.deadlydiamond98.koalalib.networking.packets.c2s.PunchBlockC2SPacket;
import net.deadlydiamond98.koalalib.networking.packets.s2c.CustomHudRendererS2CPacket;
import net.deadlydiamond98.koalalib.networking.packets.s2c.EntityGlowColorS2CPacket;
import net.deadlydiamond98.koalalib.networking.packets.s2c.EntityMagicUpdateS2CPacket;
import net.deadlydiamond98.koalalib.networking.packets.s2c.HasAdvancementS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class KoalaPackets {

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(EntityMagicUpdateS2CPacket.ID, EntityMagicUpdateS2CPacket.Handler::receive);
        ClientPlayNetworking.registerGlobalReceiver(HasAdvancementS2CPacket.ID, HasAdvancementS2CPacket.Handler::receive);
        ClientPlayNetworking.registerGlobalReceiver(CustomHudRendererS2CPacket.ID, CustomHudRendererS2CPacket.Handler::receive);
        ClientPlayNetworking.registerGlobalReceiver(EntityGlowColorS2CPacket.ID, EntityGlowColorS2CPacket.Handler::receive);
    }

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(LeftClickItemC2SPacket.ID, LeftClickItemC2SPacket.Handler::receive);
        ServerPlayNetworking.registerGlobalReceiver(PunchBlockC2SPacket.ID, PunchBlockC2SPacket.Handler::receive);
    }
}
