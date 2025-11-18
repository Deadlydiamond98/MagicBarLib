package net.deadlydiamond98.koalalib.common.advancement;

import com.google.gson.JsonObject;
import net.deadlydiamond98.koalalib.networking.s2c.AdvancementActionS2CPacket;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class CustomAdvancement extends AbstractCriterion<CustomAdvancement.Conditions> {
    public static final HashMap<Identifier, CustomAdvancement> CUSTOM_ADVANCEMENTS = new HashMap<>();
    private final Identifier id;

    public CustomAdvancement(Identifier id) {
        this.id = id;
        CUSTOM_ADVANCEMENTS.put(id, this);
    }

    public final void trigger(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            Advancement advancement = serverPlayer.getServer().getAdvancementLoader().get(this.getId());
            if (advancement != null) {
                AdvancementProgress progress = serverPlayer.getAdvancementTracker().getProgress(advancement);
                if (progress.isDone()) {
                    return;
                }
            }
            this.trigger(serverPlayer, (conditions) -> true);
            AdvancementActionS2CPacket.send(serverPlayer, getId());
            doWhenTriggered(player);
        }
    }

    public void doWhenTriggered(PlayerEntity player) {}

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new Conditions(playerPredicate);
    }

    public class Conditions extends AbstractCriterionConditions {
        public Conditions(LootContextPredicate player) {
            super(id, player);
        }
    }
}