package net.deadlydiamond98.koalalib.networking.packets.c2s;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.common.items.interaction.ISwingAction;
import net.deadlydiamond98.koalalib.util.mixindata.player.IPlayerOtherMixinData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class LeftClickItemC2SPacket {
    public static final Identifier ID = new Identifier(KoalaLib.MOD_ID, "left_click_item_packet");

    public static void send() {
        PacketByteBuf buf = PacketByteBufs.create();
        ClientPlayNetworking.send(ID, buf);
    }

    public static class Handler {
        public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            server.execute(() -> {
                World world = player.getWorld();
                Item item = player.getMainHandStack().getItem();

                if (item instanceof ISwingAction leftClickItem) {
                    leftClickItem.attack(world, player);
                    ((IPlayerOtherMixinData) player).koalalib$setAttacking(true);
                }
            });
        }
    }
}
