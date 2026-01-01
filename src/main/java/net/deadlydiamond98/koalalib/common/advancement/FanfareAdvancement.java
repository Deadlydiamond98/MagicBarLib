package net.deadlydiamond98.koalalib.common.advancement;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class FanfareAdvancement extends CustomAdvancement {
    private final SoundEvent soundEvent;

    public FanfareAdvancement(Identifier id, SoundEvent soundEvent) {
        super(id);
        this.soundEvent = soundEvent;
    }

    @Override
    public void doWhenTriggered(PlayerEntity player) {
        if (player.getWorld().isClient()) {
            player.playSound(this.soundEvent, 1, 1);
        }
    }
}
