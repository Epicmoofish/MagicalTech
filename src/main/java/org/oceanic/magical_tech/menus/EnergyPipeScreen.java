package org.oceanic.magical_tech.menus;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.oceanic.magical_tech.MagicalTech;

import java.util.ArrayList;
import java.util.List;

public class EnergyPipeScreen extends AbstractContainerScreen<EnergyPipeScreenHandler> {
    //A path to the gui texture. In this example we use the texture from the dispenser
    private static final ResourceLocation TEXTURE = new ResourceLocation(MagicalTech.MOD_ID, "textures/gui/pipes/energy_pipe.png");

    private static final int directionWidth = 13;
    private static final float directionPriorityCenter = 35.5f;
    private static final int directionSpacing = 14;
    private static final int directionLabelCenter = 20;
    private static final int blockLabelCenter = 63;
    private static final int directionIOCenter = 54;

    public EnergyPipeScreen(EnergyPipeScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.playerInventoryTitle = Component.empty();
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
    public boolean mouseClicked(double d, double e, int i) {
        return super.mouseClicked(d, e, i);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderTransparentBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        int k = this.leftPos;
        int l = this.topPos;
        RenderSystem.disableDepthTest();
        Direction[] directions = Direction.values();
        for (int i = 0; i < directions.length; i++) {
            Direction dir = directions[i];
            String priority = this.menu.priorityFor(dir);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate((float)(k + ((directionSpacing * (i + 1) + directionWidth * i) + (directionWidth - font.width(priority) / 2.0)/2.0)), (float)l + directionPriorityCenter, 0.0F);
            guiGraphics.pose().scale(0.5f, 0.5f, 0.5f);
            guiGraphics.drawString(this.font, priority, 0, 0, 0xFFFFFF, false);
            guiGraphics.pose().popPose();

            Component direction = Component.translatable("direction."+dir.getName());
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate((float)(k + ((directionSpacing * (i + 1) + directionWidth * i) + (directionWidth - font.width(direction) / 2.0)/2.0)), (float)l + directionLabelCenter, 0.0F);

            guiGraphics.pose().scale(0.5f, 0.5f, 0.5f);
            guiGraphics.drawString(this.font, direction, 0, 0, 4210752, false);
            guiGraphics.pose().popPose();

            String blockLabel = this.menu.getNameOf(dir).getString();
            float currentHeight = 0.0f;
            for (String x : splitString(blockLabel)) {
                
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate((float) (k + ((directionSpacing * (i + 1) + directionWidth * i) + (directionWidth - font.width(x)/2.0) / 2.0)), (float) l + blockLabelCenter + currentHeight, 0.0F);
                float scale = 0.5f;
                int height = font.lineHeight;

                guiGraphics.pose().scale(scale, scale, scale);
                guiGraphics.drawString(this.font, x, 0, 0, 4210752, false);
                guiGraphics.pose().popPose();
                currentHeight += height * scale;
                if (blockLabelCenter + currentHeight >= this.imageHeight - 6) break;
            }
        }
        for (int i = 0; i < directions.length; i++) {

            guiGraphics.pose().pushPose();
            Direction dir = directions[i];
            String priority = this.menu.ioFor(dir);
            int size = 3;
            if (priority.equals("O")) size = 5;
            if (priority.equals("I/O")) size = 15;
            guiGraphics.pose().translate((float)(k + (((directionSpacing * (i + 1) + directionWidth * i) + (directionWidth - size * 0.7)/2.0))), (float)l + directionIOCenter, 0.0F);
            guiGraphics.pose().scale(0.7f, 0.7f, 0.7f);
            guiGraphics.drawString(this.font, priority, 0, 0, 4210752, false);
            guiGraphics.pose().popPose();

        }
        RenderSystem.enableDepthTest();
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
    private List<String> splitString(String s) {
        ArrayList<String> s2 = new ArrayList<>();
        if (s.contains(" ")) {
            for (String str : s.split(" ")) {
                s2.addAll(splitString(str));
            }
        } else {
            String str = "";
            for (int i = 0; i<s.length(); i++) {
                String stro = str + s.charAt(i);
                if (font.width(stro) > 52) {
                    s2.add(str);
                    str = s.charAt(i) + "";
                } else {
                    str = stro;
                }
            }
            s2.add(str);
        }
        return s2;
    }
    @Override
    protected void init() {
        super.init();
        // Center the title
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }
}