package net.deadlydiamond98.koalalib.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.deadlydiamond98.koalalib.KoalaLib;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(SplashTextResourceSupplier.class)
public class SplashScreenMixin {
    @ModifyReturnValue(method = "prepare(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)Ljava/util/List;", at = @At("RETURN"))
    private List<String> koalalib$prepare(List<String> original) {
        ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();

        FabricLoader.getInstance().getAllMods().forEach(container -> {
            Identifier splashFilePath = new Identifier(container.getMetadata().getId(), "koala_splashes.txt");

            if (resourceManager.getResource(splashFilePath).isPresent()) {
                try {
                    BufferedReader bufferedReader = MinecraftClient.getInstance().getResourceManager().openAsReader(splashFilePath);
                    original.addAll(bufferedReader.lines().toList());
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return original;
    }
}
