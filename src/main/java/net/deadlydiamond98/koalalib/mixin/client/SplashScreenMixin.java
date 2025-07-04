package net.deadlydiamond98.koalalib.mixin.client;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.util.perf.Profiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// TODO: FIGURE OUT CAUSE OF CRASH

@Mixin(SplashTextResourceSupplier.class)
public class SplashScreenMixin {

//    @Unique private static final List<String> KOALA_CUSTOM_SPLASHES = new ArrayList<>();
//    @Shadow @Final private List<String> splashTexts;
//
//    @Inject(method = "apply", at = @At("TAIL"))
//    private void koalalib$addSplashes(List<String> list, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci) {
//        koalalib$addTryModSplash("healpgood", "Healing Pretty Good");
//        koalalib$addTryModSplash("zeldacraft", "The Legend of Steve");
//        koalalib$addTryModSplash("familiar_friends", "Familiar Friends");
//        this.splashTexts.addAll(KOALA_CUSTOM_SPLASHES);
//        Collections.shuffle(this.splashTexts); // Shuffles so that mine aren't more common than others
//    }
//
//    @Unique
//    private void koalalib$addTryModSplash(String id, String name) {
//        if (!KoalaLib.isModLoaded(id)) {
//            KOALA_CUSTOM_SPLASHES.add("Also try " + name + "!");
//        }
//    }
}
