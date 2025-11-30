package net.deadlydiamond98.koalalib.util;

import net.deadlydiamond98.koalalib.util.mixindata.player.IPlayerOtherMixinData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.random.Random;


public class PseudoRandom {
    private final Random random;
    private int max;
    private float chance;
    private int tries;

    public PseudoRandom(Random random, float chance, int max) {
        this.random = random;
        this.chance = chance;
        this.max = max;
    }

    public PseudoRandom(Random random, NbtCompound nbt) {
        this(random, nbt.getFloat("chance"), nbt.getInt("max"));
        this.tries = nbt.getInt("tries");
    }

    public void updateArgs(float chance, int max) {
        this.chance = chance;
        this.max = max;
    }

    public boolean isSuccessful() {
        if (this.tries++ >= this.max) {
            this.tries = 0;
            return true;
        } else if (this.random.nextFloat() <= this.chance) {
            this.tries = 0;
            return true;
        }
        return false;
    }

    public static PseudoRandom fromNbt(Random random, NbtCompound nbt) {
        return new PseudoRandom(random, nbt);
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putFloat("chance", this.chance);
        nbt.putFloat("max", this.max);
        nbt.putFloat("tries", this.tries);
        return nbt;
    }

    public static boolean getPlayerPseudoRandom(PlayerEntity player, String id, float chance, int max) {
        if (player instanceof IPlayerOtherMixinData data) {
            return data.zeldacraft$getPseudoRandom(id, chance, max);
        }
        return false;
    }
}
