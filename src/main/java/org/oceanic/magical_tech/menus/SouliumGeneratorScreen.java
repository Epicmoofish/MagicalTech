package org.oceanic.magical_tech.menus;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.oceanic.magical_tech.MagicalTech;

public class SouliumGeneratorScreen extends AbstractContainerScreen<AbstractSouliumGeneratorScreenHandler> {
    //A path to the gui texture. In this example we use the texture from the dispenser
    private static final ResourceLocation TEXTURE = new ResourceLocation(MagicalTech.MOD_ID, "textures/gui/soulium_generator.png");
    private static final ResourceLocation SOULIUM_FILLED =  new ResourceLocation(MagicalTech.MOD_ID, "textures/gui/sprites/soulium_filled.png");
    private static final ResourceLocation BURN_PROGRESS = new ResourceLocation("container/furnace/lit_progress");

    public SouliumGeneratorScreen(AbstractSouliumGeneratorScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.imageHeight = 166;
        this.imageWidth = 176;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        int n;
        int k = this.leftPos;
        int l = this.topPos;
        guiGraphics.blit(TEXTURE, k, l, 0, 0, this.imageWidth, this.imageHeight);
        n = Mth.ceil(this.menu.getSouliumPercentage() * 72.0f) + 1;
        guiGraphics.blit(SOULIUM_FILLED, k + 151, l + 7 + 73 - n, 0, 73-n, 18, n, 18, 73);
        n = Mth.ceil(this.menu.getBurnPercentage() * 13.0f) + 1;
        guiGraphics.blitSprite(BURN_PROGRESS, 14, 14, 0, 14 - n, k + 80, l + 27 + 14 - n, 14, n);
        //in 1.20 or above,this method is in DrawContext class.
    }
    @Override
    protected void slotClicked(Slot slot, int i, int j, ClickType clickType) {
        super.slotClicked(slot, i, j, clickType);
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