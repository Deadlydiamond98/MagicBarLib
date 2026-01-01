package net.deadlydiamond98.koalalib.common.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.deadlydiamond98.koalalib.networking.s2c.AdvancementActionS2CPacket;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Optional;

public class CustomAdvancement extends AbstractCriterion<CustomAdvancement.Conditions> {
    public static final HashMap<Identifier, CustomAdvancement> CUSTOM_ADVANCEMENTS = new HashMap<>();
    private final Identifier id;

    public CustomAdvancement(Identifier id) {
        this.id = id;
        CUSTOM_ADVANCEMENTS.put(id, this);
    }

    public final void trigger(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            AdvancementEntry advancement = serverPlayer.getServer().getAdvancementLoader().get(this.getId());
            if (advancement != null) {
                AdvancementProgress progress = serverPlayer.getAdvancementTracker().getProgress(advancement);
                if (progress.isDone()) {
                    return;
                }
            }
            this.trigger(serverPlayer, (conditions) -> true);
            AdvancementActionS2CPacket.Sender.send(serverPlayer, getId());
            doWhenTriggered(player);
        }
    }

    public Identifier getId() {
        return this.id;
    }

    public void doWhenTriggered(PlayerEntity player) {}

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<LootContextPredicate> player) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player")
                        .forGetter(Conditions::player)).apply(instance, Conditions::new));

        @Override
        public Optional<LootContextPredicate> player() {
            return this.player;
        }
    }
}