package net.deadlydiamond98.koalalib.networking.c2s;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.common.items.interaction.ISwingAction;
import net.deadlydiamond98.koalalib.util.mixinterfaces.player.IPlayerOtherMixinData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public record LeftClickItemC2SPacket() implements CustomPayload {
    public static final Id<LeftClickItemC2SPacket> ID = new Id<>(Identifier.of(KoalaLib.MOD_ID, "left_click_item_packet"));
    public static final PacketCodec<PacketByteBuf, LeftClickItemC2SPacket> CODEC = PacketCodec.unit(new LeftClickItemC2SPacket());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Sender {
        public static void send() {
            ClientPlayNetworking.send(new LeftClickItemC2SPacket());
        }
    }

    public static void receive(LeftClickItemC2SPacket payload, ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            World world = context.player().getWorld();
            Item item = context.player().getMainHandStack().getItem();

            if (item instanceof ISwingAction leftClickItem) {
                leftClickItem.attack(world, context.player());
                ((IPlayerOtherMixinData) context.player()).koalalib$setAttacking(true);
            }
        });
    }
}
