package net.deadlydiamond98.koalalib.networking;

import net.deadlydiamond98.koalalib.networking.c2s.LeftClickItemC2SPacket;
import net.deadlydiamond98.koalalib.networking.c2s.PunchBlockC2SPacket;
import net.deadlydiamond98.koalalib.networking.c2s.PunchEntityC2SPacket;
import net.deadlydiamond98.koalalib.networking.s2c.AdvancementActionS2CPacket;
import net.deadlydiamond98.koalalib.networking.s2c.CustomHudRendererS2CPacket;
import net.deadlydiamond98.koalalib.networking.s2c.EntityMagicUpdateS2CPacket;
import net.deadlydiamond98.koalalib.networking.s2c.HasAdvancementS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class KoalaPackets {

    public static class Server {
        public static void registerNetworkingServer() {

            // SERVER TO CLIENT
            PayloadTypeRegistry.playS2C().register(AdvancementActionS2CPacket.ID, AdvancementActionS2CPacket.CODEC);
            PayloadTypeRegistry.playS2C().register(HasAdvancementS2CPacket.ID, HasAdvancementS2CPacket.CODEC);
            PayloadTypeRegistry.playS2C().register(EntityMagicUpdateS2CPacket.ID, EntityMagicUpdateS2CPacket.CODEC);
            PayloadTypeRegistry.playS2C().register(CustomHudRendererS2CPacket.ID, CustomHudRendererS2CPacket.CODEC);

            // CLIENT TO SERVER
            PayloadTypeRegistry.playC2S().register(LeftClickItemC2SPacket.ID, LeftClickItemC2SPacket.CODEC);
            PayloadTypeRegistry.playC2S().register(PunchBlockC2SPacket.ID, PunchBlockC2SPacket.CODEC);
            PayloadTypeRegistry.playC2S().register(PunchEntityC2SPacket.ID, PunchEntityC2SPacket.CODEC);

            // SERVER RECEIVER
            ServerPlayNetworking.registerGlobalReceiver(LeftClickItemC2SPacket.ID, LeftClickItemC2SPacket::receive);
            ServerPlayNetworking.registerGlobalReceiver(PunchBlockC2SPacket.ID, PunchBlockC2SPacket::receive);
            ServerPlayNetworking.registerGlobalReceiver(PunchEntityC2SPacket.ID, PunchEntityC2SPacket::receive);
        }
    }

    public static class Client {
        public static void registerNetworkingClient() {
            // CLIENT RECEIVER
            ClientPlayNetworking.registerGlobalReceiver(AdvancementActionS2CPacket.ID, AdvancementActionS2CPacket::receive);
            ClientPlayNetworking.registerGlobalReceiver(HasAdvancementS2CPacket.ID, HasAdvancementS2CPacket::receive);
            ClientPlayNetworking.registerGlobalReceiver(EntityMagicUpdateS2CPacket.ID, EntityMagicUpdateS2CPacket::receive);
            ClientPlayNetworking.registerGlobalReceiver(CustomHudRendererS2CPacket.ID, CustomHudRendererS2CPacket::receive);
        }
    }
}
