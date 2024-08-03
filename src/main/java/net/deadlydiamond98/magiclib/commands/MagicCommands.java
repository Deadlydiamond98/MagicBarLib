package net.deadlydiamond98.magiclib.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.Iterator;

public class MagicCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            setManaCommand(dispatcher, registryAccess, environment);
            setMaxManaCommand(dispatcher, registryAccess, environment);
        });
    }

    private static void setManaCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("setmana").requires((source) -> {
            return source.hasPermissionLevel(2);
        })
                .then(CommandManager.argument("targets", EntityArgumentType.players())
                        .then(CommandManager.argument("count", IntegerArgumentType.integer(0)).executes((context) -> {
                            return executeSetMana((ServerCommandSource)context.getSource(), EntityArgumentType.getEntities(context, "targets"),
                                    IntegerArgumentType.getInteger(context, "count"));
                        }
        ))));
    }

    private static int executeSetMana(ServerCommandSource source, Collection<? extends Entity> targets, int count) {

        if (count < 0) {
            return 0;
        }

        Iterator targetss = targets.iterator();

        while(targetss.hasNext()) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)targetss.next();
            serverPlayerEntity.setMana(count);
        }

        if (targets.size() == 1) {
            source.sendFeedback(() -> {
                return Text.translatable("commands.manaSet.success.single", new Object[]{((Entity)targets.iterator().next()).getDisplayName(), count});
            }, true);
        } else {
            source.sendFeedback(() -> {
                return Text.translatable("commands.manaSet.success.multiple", new Object[]{targets.size(), count});
            }, true);
        }

        return targets.size();
    }

    private static void setMaxManaCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("setmaxmana").requires((source) -> {
                    return source.hasPermissionLevel(2);
                })
                .then(CommandManager.argument("targets", EntityArgumentType.players())
                        .then(CommandManager.argument("count", IntegerArgumentType.integer(1)).executes((context) -> {
                                    return executeSetMaxMana((ServerCommandSource)context.getSource(), EntityArgumentType.getEntities(context, "targets"),
                                            IntegerArgumentType.getInteger(context, "count"));
                                }
                        ))));
    }

    private static int executeSetMaxMana(ServerCommandSource source, Collection<? extends Entity> targets, int count) {

        if (count < 1) {
            return 0;
        }

        Iterator targetss = targets.iterator();

        while(targetss.hasNext()) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)targetss.next();
            serverPlayerEntity.setMaxMana(count);
        }

        if (targets.size() == 1) {
            source.sendFeedback(() -> {
                return Text.translatable("commands.manaSet.success.single", new Object[]{((Entity)targets.iterator().next()).getDisplayName(), count});
            }, true);
        } else {
            source.sendFeedback(() -> {
                return Text.translatable("commands.manaSet.success.multiple", new Object[]{targets.size(), count});
            }, true);
        }

        return targets.size();
    }
}
