package net.deadlydiamond98.koalalib.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.deadlydiamond98.koalalib.networking.packets.s2c.EntityGlowColorS2CPacket;
import net.deadlydiamond98.koalalib.util.mixindata.ICustomGlowingMixinData;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(Entity.class)
public abstract class EntityGlowingMixin implements ICustomGlowingMixinData {

    @Unique private Optional<Integer> koalalib$glowColor = Optional.empty();

    @ModifyReturnValue(method = "getTeamColorValue", at = @At("RETURN"))
    private int way$getTeamColor(int original) {
        if (koalalib$getGlowColor().isPresent()) {
            return koalalib$getGlowColor().get();
        }
        return original;
    }

    @ModifyReturnValue(method = "isGlowing", at = @At("RETURN"))
    private boolean way$isCurrentlyGlowing(boolean original) {
        if (koalalib$getGlowColor().isPresent()) {
            return true;
        }
        return original;
    }

    @Override
    public Optional<Integer> koalalib$getGlowColor() {
        return this.koalalib$glowColor;
    }

    @Override
    public void koalalib$setGlowColor(Optional<Integer> hex) {
        this.koalalib$glowColor = hex;

        Entity entity = (Entity) (Object) this;

        if (!entity.getWorld().isClient) {
            entity.getWorld().getPlayers().forEach(player -> {
                if (player instanceof ServerPlayerEntity serverPlayer) {
                    EntityGlowColorS2CPacket.send(serverPlayer, entity.getId(), hex);
                }
            });
        }
    }
}
