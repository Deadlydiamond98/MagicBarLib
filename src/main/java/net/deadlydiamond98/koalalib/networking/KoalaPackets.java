package net.deadlydiamond98.koalalib.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.apache.logging.log4j.core.jmx.Server;

public class KoalaPackets {

    public static void registerNetworkingServer() {

        //SERVER TO CLIENT
//        PayloadTypeRegistry.playS2C().register(SyncCompanionPlayerDataPacket.ID, SyncCompanionPlayerDataPacket.CODEC);
//        PayloadTypeRegistry.playS2C().register(SyncCompanionDataPacket.ID, SyncCompanionDataPacket.CODEC);
//        PayloadTypeRegistry.playS2C().register(OpenCompanionBookPacket.ID, OpenCompanionBookPacket.CODEC);

        // CLIENT TO SERVER
        PayloadTypeRegistry.playC2S().register(LeftClickItemC2SPacket.ID, LeftClickItemC2SPacket.CODEC);

        // Receiver
        ServerPlayNetworking.registerGlobalReceiver(LeftClickItemC2SPacket.ID, LeftClickItemC2SPacket::recieve);
    }

    public static void registerNetworkingClient() {

    }

//    public static void registerS2CPackets() {
//        ClientPlayNetworking.registerGlobalReceiver(EntityMagicUpdateS2CPacket.ID, EntityMagicUpdateS2CPacket.Handler::receive);
//        ClientPlayNetworking.registerGlobalReceiver(HasAdvancementS2CPacket.ID, HasAdvancementS2CPacket.Handler::receive);
//        ClientPlayNetworking.registerGlobalReceiver(CustomHudRendererS2CPacket.ID, CustomHudRendererS2CPacket.Handler::receive);
//        ClientPlayNetworking.registerGlobalReceiver(AdvancementActionS2CPacket.ID, AdvancementActionS2CPacket.Handler::receive);
//    }
//
//    public static void registerC2SPackets() {
//        ServerPlayNetworking.registerGlobalReceiver(LeftClickItemC2SPacket.ID, LeftClickItemC2SPacket.Handler::receive);
//        ServerPlayNetworking.registerGlobalReceiver(PunchBlockC2SPacket.ID, PunchBlockC2SPacket.Handler::receive);
//        ServerPlayNetworking.registerGlobalReceiver(PunchEntityC2SPacket.ID, PunchEntityC2SPacket.Handler::receive);
//    }

    private static class Server {
        public static void registerC2SPacket() {
            PayloadTypeRegistry.playC2S().register();
            ServerPlayNetworking.registerGlobalReceiver()
        }
    }
}
