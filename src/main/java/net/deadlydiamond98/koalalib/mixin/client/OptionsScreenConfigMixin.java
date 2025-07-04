package net.deadlydiamond98.koalalib.mixin.client;

import net.deadlydiamond98.koalalib.client.screen.config.KoalaConfigScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenConfigMixin extends Screen {
    @Shadow @Final private GameOptions settings;

    @Shadow protected abstract ButtonWidget createButton(Text message, Supplier<Screen> screenSupplier);

    protected OptionsScreenConfigMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("HEAD"),method = "init")
    private void koalalib$init(CallbackInfo ci) {
        if (client != null) {
            ButtonWidget configButton = this.createButton(
                    Text.translatable("koalalib.menu.configMenu"),
                    () -> new KoalaConfigScreen(this, this.settings)
            );

            configButton.setX(this.width - 55);
            configButton.setY(this.height - 25);
            configButton.setWidth(50);

            this.addDrawableChild(
                    configButton
            );
        }
    }
}
