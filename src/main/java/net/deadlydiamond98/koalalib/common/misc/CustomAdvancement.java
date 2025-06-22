package net.deadlydiamond98.koalalib.common.misc;

import com.google.gson.JsonObject;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class CustomAdvancement extends AbstractCriterion<CustomAdvancement.Conditions> {

    private final Identifier id;

    public CustomAdvancement(Identifier id) {
        this.id = id;
    }

    public void trigger(ServerPlayerEntity player) {
        Advancement advancement = player.getServer().getAdvancementLoader().get(this.getId());
        if (advancement != null) {
            AdvancementProgress progress = player.getAdvancementTracker().getProgress(advancement);
            if (progress.isDone()) {
                return;
            }
        }
        this.trigger(player, (conditions) -> true);
    }

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