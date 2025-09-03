package net.deadlydiamond98.koalalib.client.events;

import net.deadlydiamond98.koalalib.common.blocks.interaction.IHitBlockAction;
import net.deadlydiamond98.koalalib.common.items.interaction.ISwingAction;
import net.deadlydiamond98.koalalib.networking.packets.c2s.LeftClickItemC2SPacket;
import net.deadlydiamond98.koalalib.networking.packets.c2s.PunchBlockC2SPacket;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class KoalaClientTickEvents {

    public static boolean wasAttacking = false;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(KoalaClientTickEvents::endClientTick);
    }

    private static void endClientTick(MinecraftClient client) {
        PlayerEntity player = client.player;

        if (player != null) {
            boolean isAttacking = client.options.attackKey.isPressed();
            Item item = player.getMainHandStack().getItem();
            World world = player.getWorld();

            if (isAttacking) {
                handleBlockAtkAction(client, world);
                if (!wasAttacking) {
                    handleItemAtkAction(item, world, player);
                    wasAttacking = true;
                }
            } else if (wasAttacking) {
                wasAttacking = false;
            }
        }
    }

    private static void handleItemAtkAction(Item item, World world, PlayerEntity player) {
        if (item instanceof ISwingAction swingAction) {
            swingAction.attack(world, player);
            LeftClickItemC2SPacket.send();
        }
    }

    private static void handleBlockAtkAction(MinecraftClient client, World world) {
        BlockHitResult hitResult = (BlockHitResult) client.crosshairTarget;
        if (hitResult != null) {
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = world.getBlockState(pos);

            if (state.getBlock() instanceof IHitBlockAction hitBlock) {
                hitBlock.attemptAttack(wasAttacking, state, pos, world, client.player);
                PunchBlockC2SPacket.send(wasAttacking, pos);
            }
        }
    }
}
