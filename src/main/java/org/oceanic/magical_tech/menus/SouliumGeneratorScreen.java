package org.oceanic.magical_tech.menus;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.oceanic.magical_tech.MagicalTech;

public class SouliumGeneratorScreen extends AbstractContainerScreen<AbstractSouliumGeneratorScreenHandler> {
    //A path to the gui texture. In this example we use the texture from the dispenser
    private static final ResourceLocation TEXTURE = new ResourceLocation(MagicalTech.MOD_ID, "textures/gui/pipes/energy_pipe.png");

    public SouliumGeneratorScreen(AbstractSouliumGeneratorScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.imageHeight = 85;
        this.imageWidth = 176;
    }

    @Override
    protected void renderBg(GuiGraphics matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - this.imageWidth) / 2;
        int y = (height - this.imageHeight) / 2;
        matrices.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        //in 1.20 or above,this method is in DrawContext class.
    }

    @Override
    public boolean mouseClicked(double d, double e, int i2) {
        return super.mouseClicked(d, e, i2);
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderTransparentBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
    @Override
    protected void init() {
        super.init();
        // Center the title
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }
}