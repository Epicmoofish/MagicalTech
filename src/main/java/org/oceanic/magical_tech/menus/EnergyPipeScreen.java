package org.oceanic.magical_tech.menus;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
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
    private static final int blockLabelCenter = 65;
    private static final int directionIOCenter = 54;
    private static final int squareStart = 14;
    private static final int squareY = 50;
    private static final int squareSize = 13;
    private static final int squareNext = 27;


    private static final int downArrowY = 42;
    private static final int arrowHeight = 7;
    private static final int upArrowY = 25;

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
    public boolean mouseClicked(double d, double e, int i2) {
        for (int i = 0; i < 6; i++) {
            Direction dir = Direction.values()[i];
            if (this.isHovering(squareStart + squareNext * i, squareY, squareSize, squareSize, d, e)) {
                this.menu.modifyIo(dir);
            }
            if (this.isHovering(squareStart + squareNext * i, downArrowY, squareSize, arrowHeight, d, e)) {
                this.menu.addPriority(dir, Screen.hasShiftDown() ? -10 : -1);
            }
            if (this.isHovering(squareStart + squareNext * i, upArrowY, squareSize, arrowHeight, d, e)) {
                this.menu.addPriority(dir, Screen.hasShiftDown() ? 10 : 1);
            }
        }
        return super.mouseClicked(d, e, i2);
    }

    @Override
    public void onClose() {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(this.menu.blockPos);
        buf.writeInt(this.menu.getPriority(Direction.UP));
        buf.writeInt(this.menu.getPriority(Direction.DOWN));
        buf.writeInt(this.menu.getPriority(Direction.NORTH));
        buf.writeInt(this.menu.getPriority(Direction.SOUTH));
        buf.writeInt(this.menu.getPriority(Direction.EAST));
        buf.writeInt(this.menu.getPriority(Direction.WEST));

        buf.writeInt(this.menu.getIoValue(Direction.UP));
        buf.writeInt(this.menu.getIoValue(Direction.DOWN));
        buf.writeInt(this.menu.getIoValue(Direction.NORTH));
        buf.writeInt(this.menu.getIoValue(Direction.SOUTH));
        buf.writeInt(this.menu.getIoValue(Direction.EAST));
        buf.writeInt(this.menu.getIoValue(Direction.WEST));
        ClientPlayNetworking.send(MagicalTech.ENERGY_PIPE_PACKET, buf);
        super.onClose();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderTransparentBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        int k = this.leftPos;
        int l = this.topPos;
        RenderSystem.disableDepthTest();
        Direction[] directions = Direction.values();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((float)k, (float)l, 0.0F);
        for (int i = 0; i < 6; i++) {
            if (this.isHovering(squareStart + squareNext * i, squareY, squareSize, squareSize, mouseX, mouseY)) {
                guiGraphics.fillGradient(RenderType.guiOverlay(), squareStart + squareNext * i, squareY, squareStart + squareNext * i + squareSize, squareY + squareSize, 0x8000d5ff, 0x8000d5ff, 0);
            }
            if (this.isHovering(squareStart + squareNext * i, downArrowY, squareSize, arrowHeight, mouseX, mouseY)) {
                guiGraphics.fillGradient(RenderType.guiOverlay(), squareStart + squareNext * i, downArrowY, squareStart + squareNext * i + squareSize, downArrowY + arrowHeight, 0x8000d5ff, 0x8000d5ff, 0);
            }
            if (this.isHovering(squareStart + squareNext * i, upArrowY, squareSize, arrowHeight, mouseX, mouseY)) {
                guiGraphics.fillGradient(RenderType.guiOverlay(), squareStart + squareNext * i, upArrowY, squareStart + squareNext * i + squareSize, upArrowY + arrowHeight, 0x8000d5ff, 0x8000d5ff, 0);
            }
        }
        guiGraphics.pose().popPose();
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
                guiGraphics.pose().translate((float) (k + ((directionSpacing * (i + 1) + directionWidth * i) + (directionWidth - font.width(x)/2.0/1.25) / 2.0/1.25)), (float) l + blockLabelCenter + currentHeight, 0.0F);
                float scale = 0.5f/1.25f;
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
                if (font.width(stro) > 65) {
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