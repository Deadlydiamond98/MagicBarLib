package net.deadlydiamond98.magiclib.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.deadlydiamond98.magiclib.MagicLib;
import net.deadlydiamond98.magiclib.util.MagicConfig;
import net.deadlydiamond98.magiclib.util.ManaEntityData;
import net.deadlydiamond98.magiclib.util.ColorAndAlphaInterpolator;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.GameMode;

public class ManaHudOverlay implements HudRenderCallback {

    private static final ColorAndAlphaInterpolator colorInterpolator = new ColorAndAlphaInterpolator( 0xFF00FF5C, 0xFFFFFFFF,
            0.0f, 1.0f, 5.0f);
    private static final Identifier Filled_Mana = Identifier.of(MagicLib.MOD_ID, "textures/gui/mana_full.png");
    private static final Identifier Filled_Mana_Second = Identifier.of(MagicLib.MOD_ID, "textures/gui/mana_full_second.png");
    private static final Identifier Empty_Mana = Identifier.of(MagicLib.MOD_ID, "textures/gui/mana_empty.png");


    private static float displayedMana = 0.0f;
    private static boolean transitionMinMax = false;
    private static float currentAlpha = 1.0f;
    private static float globalAlpha = 1.0f;
    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        MatrixStack matrices = drawContext.getMatrices();
        TextRenderer textRenderer = client.textRenderer;
        float tickDelta = tickCounter.getLastFrameDuration();

        // if not in survival or adventure, don't render the magic bar
        if (client.player == null || client.interactionManager.getCurrentGameMode() == GameMode.CREATIVE ||
                client.interactionManager.getCurrentGameMode() == GameMode.SPECTATOR || MagicConfig.renderManaBar == MagicConfig.manaBarEnum.Never) {
            return;
        }

        // Make Mana Bar go away when not used
        if (MagicConfig.renderManaBar == MagicConfig.manaBarEnum.When_Needed && client.player.getWhenNeededRenderTime() <= 0) {
            if (globalAlpha > 0.01f) {
                globalAlpha = MathHelper.lerp(tickDelta * 0.1f, globalAlpha, 0.0f);
            }
            else {
                globalAlpha = 0.0f;
            }
        }
        else {
            if (globalAlpha < 0.91f) {
                globalAlpha = MathHelper.lerp(tickDelta * 0.1f, globalAlpha, 1.0f);
            }
            else {
                globalAlpha = 1.0f;
            }
        }

        if (globalAlpha > 0.1f) {
            matrices.push();

            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            int mana_x = (width / 2) - 320 + MagicConfig.manaBarPositionX;
            int mana_y = height - 42 - MagicConfig.manaBarPositionY;

            // render the empty texture
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, globalAlpha);
            RenderSystem.setShaderTexture(0, Empty_Mana);
            drawContext.drawTexture(Empty_Mana, mana_x, mana_y, 0, 0, 16, 42, 16, 42);

            // get values for determining where the level will be at
            int currentMana = client.player.getMana();
            int maxMana = client.player.getMaxMana();

            // lerp so it has a nice smooth looking transition
            displayedMana = MathHelper.lerp(tickDelta * 0.25f, displayedMana, currentMana);

            if (displayedMana > maxMana) {
                displayedMana = maxMana;
            }

            int filledHeight = (int) ((displayedMana / (float) maxMana) * 33);


            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            // update alpha when at max
            currentAlpha = colorInterpolator.updateAndGetAlpha(tickDelta);

            // reg mana bar
            RenderSystem.setShaderTexture(0, Filled_Mana);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, globalAlpha * currentAlpha);
            drawContext.drawTexture(Filled_Mana, mana_x + 4, mana_y + 4 + (33 - filledHeight), 0, 33 - filledHeight, 8, filledHeight, 8, 33);

            // max mana bar
            RenderSystem.setShaderTexture(0, Filled_Mana_Second);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, globalAlpha * (1.0f - currentAlpha));
            drawContext.drawTexture(Filled_Mana_Second, mana_x + 4, mana_y + 4 + (33 - filledHeight), 0, 33 - filledHeight, 8, filledHeight, 8, 33);

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.disableBlend();

            matrices.push();

            String displayZeros = "";

            // zeros to display so numbers are always 3 digits
            if (Math.round(displayedMana) < 10) {
                displayZeros = "00";
            } else if (Math.round(displayedMana) < 100) {
                displayZeros = "0";

            }

            // enable transitions if max / not max
            if (currentMana == maxMana && !transitionMinMax) {
                colorInterpolator.resetTransition(colorInterpolator.updateAndGetColor(tickDelta, globalAlpha), 0xFF00FF5C, currentAlpha, 0.0f, 40.0f);
                transitionMinMax = true;
            } else if (currentMana != maxMana && transitionMinMax) {
                colorInterpolator.resetTransition(colorInterpolator.updateAndGetColor(tickDelta, globalAlpha), 0xFFFFFFFF, currentAlpha, 1.0f, 40.0f);
                transitionMinMax = false;
            }

            // number text
            Text filledAmountText =
                    Text.literal(displayZeros + Math.round(displayedMana) + " / " + maxMana).setStyle(Style.EMPTY.withFont(MagicLib.ZELDA_FONT));
            matrices.translate(mana_x + MagicConfig.manaBarTextOffsetX, mana_y + MagicConfig.manaBarTextOffsetY, 0);
            matrices.scale(0.75f, 0.75f, 0.75f);

            // change color of text
            int currentColor = colorInterpolator.updateAndGetColor(tickDelta, globalAlpha);

            drawContext.drawText(textRenderer, filledAmountText, 0, 0, currentColor, false);

//        MagicLib.LOGGER.info(globalAlpha + "  " + currentColor);

            matrices.pop();
            matrices.pop();
        }
    }
}
