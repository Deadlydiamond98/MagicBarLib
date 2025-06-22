package net.deadlydiamond98.koalalib.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.deadlydiamond98.koalalib.util.magic.MagicBarHelper;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

import java.util.Collection;

public class MagicBarCommands {

    private static final Pair<String, Integer> REG_STRING = new Pair<>("lvl", 0);
    private static final Pair<String, Integer> MAX_STRING = new Pair<>("max", 1);

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            setManaCommand(dispatcher, REG_STRING);
            setManaCommand(dispatcher, MAX_STRING);
        });
    }

    private static void setManaCommand(CommandDispatcher<ServerCommandSource> dispatcher, Pair<String, Integer> setManaType) {
        dispatcher.register(CommandManager.literal("setmana").requires((source) -> source.hasPermissionLevel(2))
                .then(CommandManager.literal(setManaType.getLeft())
                        .then(CommandManager.argument("targets", EntityArgumentType.players())
                                .then(CommandManager.argument("count", IntegerArgumentType.integer(setManaType.getRight()))
                                        .executes(context -> executeManaCommand(
                                                setManaType,
                                                context.getSource(),
                                                EntityArgumentType.getEntities(context, "targets"),
                                                IntegerArgumentType.getInteger(context, "count")
                                        ))))));
    }

    private static int executeManaCommand(Pair<String, Integer> setManaType, ServerCommandSource source, Collection<? extends Entity> targets, int count) {
        boolean settingReg = setManaType.equals(REG_STRING);

        if (count < setManaType.getRight()) {
            return 0;
        }

        for (Entity target : targets) {
            ServerPlayerEntity player = (ServerPlayerEntity) target;

            if (settingReg) {
                MagicBarHelper.setMana(player, Math.min(count, MagicBarHelper.getMaxMana(player)));
            } else {
                MagicBarHelper.setMaxMana(player, count);
            }
        }


        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.koalalib.manaSet.success.single", targets.iterator().next().getDisplayName(), count), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.koalalib.manaSet.success.multiple", targets.size(), count), true);
        }
        return targets.size();
    }
}
