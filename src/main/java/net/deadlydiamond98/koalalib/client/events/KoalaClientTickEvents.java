package net.deadlydiamond98.koalalib.client.events;

import net.deadlydiamond98.koalalib.common.blocks.interaction.IHitBlockAction;
import net.deadlydiamond98.koalalib.common.entity.IHitEntityAction;
import net.deadlydiamond98.koalalib.common.items.interaction.ISwingAction;
import net.deadlydiamond98.koalalib.config.KoalaLibConfigs;
import net.deadlydiamond98.koalalib.networking.packets.c2s.LeftClickItemC2SPacket;
import net.deadlydiamond98.koalalib.networking.packets.c2s.PunchBlockC2SPacket;
import net.deadlydiamond98.koalalib.networking.packets.c2s.PunchEntityC2SPacket;
import net.deadlydiamond98.koalalib.updater.KoalaUpdateChecker;
import net.deadlydiamond98.koalalib.util.mixindata.player.IPlayerOtherMixinData;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class KoalaClientTickEvents {

    public static boolean wasAttacking = false;

    public static boolean updateTipSent = false;
    public static int updateTipTimer = 0;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(KoalaClientTickEvents::endClientTick);
    }

    private static void endClientTick(MinecraftClient client) {
        PlayerEntity player = client.player;

        if (player != null) {
            // Attack related things
            boolean isAttacking = client.options.attackKey.isPressed();
            Item item = player.getMainHandStack().getItem();
            World world = player.getWorld();

            if (isAttacking) {
                handleTargetAtkAction(client, world);
                if (!wasAttacking) {
                    handleAtkAction(item, world, player);
                    ((IPlayerOtherMixinData) player).koalalib$setAttacking(true);
                    wasAttacking = true;
                }
            } else if (wasAttacking) {
                wasAttacking = false;
            }

            // Update Chat Message

            if (!updateTipSent && KoalaLibConfigs.Main.checkForUpdates && !KoalaUpdateChecker.MOD_UPDATE_LIST.isEmpty() && updateTipTimer++ > 100) {
                player.sendMessage(Text.translatable("chat.koalalib.update.disable", Text.literal("Koala Lib").formatted(Formatting.YELLOW)).formatted(Formatting.GREEN));
                updateTipSent = true;
            }
        }
    }

    private static void handleAtkAction(Item item, World world, PlayerEntity player) {
        if (item instanceof ISwingAction swingAction) {
            swingAction.attack(world, player);
        }
        ((IPlayerOtherMixinData) player).koalalib$setAttacking(true);
        LeftClickItemC2SPacket.send();
    }

    private static void handleTargetAtkAction(MinecraftClient client, World world) {
        HitResult hitResult = client.crosshairTarget;
        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = ((BlockHitResult)  hitResult).getBlockPos();
                BlockState state = world.getBlockState(pos);

                if (state.getBlock() instanceof IHitBlockAction hitBlock) {
                    hitBlock.attemptAttack(wasAttacking, state, pos, world, client.player);
                    PunchBlockC2SPacket.send(wasAttacking, pos);
                }
            } else if (hitResult.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) hitResult).getEntity();

                if (entity instanceof IHitEntityAction hitEntity) {
                    hitEntity.attemptAttack(wasAttacking, entity, world, client.player);
                    PunchEntityC2SPacket.send(wasAttacking, entity);
                }
            }
        }
    }
}
