package net.deadlydiamond98.koalalib.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.ToggleableContent;
import net.deadlydiamond98.koalalib.util.ColorHelper;
import net.deadlydiamond98.koalalib.util.magic.MagicBarHelper;
import net.deadlydiamond98.koalalib.util.MagicConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;

public class MagicBarHud implements HudRenderCallback {

    private static final Identifier MAGIC_BAR_TEXTURE = new Identifier(KoalaLib.MOD_ID, "textures/gui/magic_bar.png");
    private static float displayedManaLvl;

    private static final float LERP_VALUE = 0.05f;

    // Text Colors
    private static final int START_COLOR = 0xFFFFFFFF;
    private static final int END_COLOR = 0xFF00FF5C;
    private static int currentColor = 0xFFFFFFFF; // Color that the text uses

    // ALPHA VALUES
    private static final float START_ALPHA = 1;
    private static final float END_ALPHA = 0;
    private static float centerBarAlpha = 1; // used for alpha transition from min to max bar texture
    private static float globalAlpha = 1; // used for transparency for everything

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        MatrixStack matrices = drawContext.getMatrices();

        updateAlphaForRenderSometimes(client);

        if (!shouldRender(client)) {
            return;
        }

        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        int posX = (width / 2) - 320 + MagicConfig.manaBarPositionX;
        int posY = height - 42 - MagicConfig.manaBarPositionY;

        renderMagicBar(drawContext, matrices, client, posX, posY, tickDelta);
    }

    /**
     * Render Method for the Magic Bar
     */
    private void renderMagicBar(DrawContext drawContext, MatrixStack matrices, MinecraftClient client, int posX, int posY, float tickDelta) {
        matrices.push();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Empty Magic Bar
        renderBarPart(drawContext, posX, posY, 0, 0, 42, globalAlpha);

        // Magic Bar Level
        updateDisplayedManaLvl(client, tickDelta);
        updateCenterAlpha(client);
        int displayHeight = getDisplayHeight(client);

        renderBarPart(drawContext, posX, posY + 4 + (33 - displayHeight), 16, 33 - displayHeight + 4, // Reg Color
                displayHeight, (float) (globalAlpha * Math.ceil(centerBarAlpha))
        );
        renderBarPart(drawContext, posX, posY + 4 + (33 - displayHeight), 32, 33 - displayHeight + 4, // Max Color
                displayHeight, globalAlpha * (START_ALPHA - centerBarAlpha)
        );

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        renderMagicBarText(drawContext, matrices, client, posX, posY);

        matrices.pop();
    }

    /**
     * Method for rendering the Text used on the Magic Bar
     */
    private void renderMagicBarText(DrawContext drawContext, MatrixStack matrices, MinecraftClient client, int posX, int posY) {
        TextRenderer textRenderer = client.textRenderer;

        String displayManaText = Math.round(displayedManaLvl) + " / " + getMaxMana(client);
        String displayedZeros = "";

        if (Math.round(displayedManaLvl) < 10) {
            displayedZeros = "00";
        } else if (Math.round(displayedManaLvl) < 100) {
            displayedZeros = "0";
        }

        Text magicLvlText = Text.literal(displayedZeros + displayManaText).setStyle(Style.EMPTY.withFont(KoalaLib.ZELDA_FONT));

        matrices.translate(posX + MagicConfig.manaBarTextOffsetX, posY + MagicConfig.manaBarTextOffsetY, 0);
        matrices.scale(0.75f, 0.75f, 0.75f);

        drawContext.drawText(textRenderer, magicLvlText, 0, 0, getTextColor(client), false);
    }

    /**
     * Draws Magic Bar Textures
     */
    private void renderBarPart(DrawContext drawContext, int posX, int posY, int u, int v, int height, float alpha) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
        RenderSystem.setShaderTexture(0, MAGIC_BAR_TEXTURE);
        drawContext.drawTexture(MAGIC_BAR_TEXTURE, posX, posY, u, v, 16, height, 48, 42);
    }

    /**
     * Method that updates the transparency of the Magic Bar when using the "When Using" Config option
     */
    private void updateAlphaForRenderSometimes(MinecraftClient client) {

        boolean isWhenNeededMode = MagicConfig.renderManaBar == MagicConfig.manaBarEnum.When_Needed;
        boolean cooldownGoesAway = MagicBarHelper.getBar(client.player).koalalib$getMagicBarRenderTime() <= 0;

        float end = isWhenNeededMode && cooldownGoesAway ? END_ALPHA : START_ALPHA;
        globalAlpha = MathHelper.lerp(LERP_VALUE, globalAlpha, end);
        globalAlpha = globalAlpha < 0.01f ? 0 : globalAlpha;
        KoalaLib.LOGGER.info(globalAlpha + "");
    }

    /**
     * Updates the transparency of the Max Mana Bar Texture (so that it changes smoothly to the new one)
     */
    private void updateCenterAlpha(MinecraftClient client) {
        float end = getManaLvl(client) >= getMaxMana(client) ? END_ALPHA : START_ALPHA;
        centerBarAlpha = MathHelper.lerp(LERP_VALUE, centerBarAlpha, end);
        centerBarAlpha = centerBarAlpha < 0.01f ? 0 : centerBarAlpha;
    }

    /**
     * Get the hex color of the Magic Bar Text
     */
    private int getTextColor(MinecraftClient client) {
        int end = getManaLvl(client) >= getMaxMana(client) ? END_COLOR : START_COLOR;
        currentColor = ColorHelper.lerpHexColor(currentColor, end, LERP_VALUE, Math.max(0.05f, globalAlpha));
        return currentColor;
    }

    /**
     * Gets the visual height of the Magic Bar
     */
    private int getDisplayHeight(MinecraftClient client) {
        return (int) ((displayedManaLvl / (float) getMaxMana(client)) * 33);
    }

    /**
     * Updates the visual height of the Magic Bar
     */
    public void updateDisplayedManaLvl(MinecraftClient client, float tickDelta) {
        displayedManaLvl = MathHelper.lerp(tickDelta * 0.25f, displayedManaLvl, getManaLvl(client));
        displayedManaLvl = Math.min(displayedManaLvl, getMaxMana(client));
    }

    /**
     * Gets the player's Mana level
     */
    private int getManaLvl(MinecraftClient client) {
        return MagicBarHelper.getMana(client.player);
    }

    /**
     * Gets the player's Max Mana level
     */
    private int getMaxMana(MinecraftClient client) {
        return MagicBarHelper.getMaxMana(client.player);
    }

    /**
     * Checks whether the Magic Bar can show up on screen
     */
    public boolean shouldRender(MinecraftClient client) {
        PlayerEntity player = client.player;
        GameMode currentGamemode = client.interactionManager.getCurrentGameMode();
        return player != null && currentGamemode.isSurvivalLike() && globalAlpha > 0
                && MagicConfig.renderManaBar != MagicConfig.manaBarEnum.Never
                && ToggleableContent.isMagicBarEnabled();
    }
}
