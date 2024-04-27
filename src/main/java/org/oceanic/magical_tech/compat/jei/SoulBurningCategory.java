package org.oceanic.magical_tech.compat.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.oceanic.magical_tech.MagicalTech;

public class SoulBurningCategory implements IRecipeCategory<SoulBurningRecipe> {
    private final IDrawableStatic background;
    private final IDrawable icon;
    private final IDrawableStatic slotDrawable;

    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public SoulBurningCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation("jei", "textures/jei/gui/gui_vanilla.png");
        this.background = guiHelper.createBlankDrawable(132, 20);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(MagicalTech.CRUDE_SOULIUM_GENERATOR));
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public @NotNull IDrawableAnimated load(@NotNull Integer cookTime) {
                        return guiHelper.drawableBuilder(location, 82, 128, 24, 17)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
        this.slotDrawable = guiHelper.getSlotDrawable();
    }
    protected IDrawableAnimated getArrow(SoulBurningRecipe recipeHolder) {
        long burnTime = recipeHolder.getAmount();
        burnTime /= recipeHolder.getMult();
        burnTime = burnTime / 10;
        if (burnTime < 2) burnTime = 2;
        burnTime = Math.min(burnTime, 500);
        int burnTimeInt = (int) burnTime;
        return this.cachedArrows.getUnchecked(burnTimeInt);
    }
    @Override
    public @NotNull RecipeType<SoulBurningRecipe> getRecipeType() {
        return RecipeTypes.SOUL_BURNING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(MagicalTech.MOD_ID + ".soul_burning_recipes.title");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull SoulBurningRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStack(recipe.getItem().getDefaultInstance());
    }
    @Override
    public void draw(SoulBurningRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        // Draw Drops
        this.slotDrawable.draw(guiGraphics, 0, 0);
        // Draw entity name
        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(guiGraphics, 24, 1);
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        Font font = Minecraft.getInstance().font;
        String text = MagicalTech.souliumString(recipe.burnAmount(), Screen.hasShiftDown()) + " DS";
        poseStack.translate(90 - font.width(text) / 2.0, 0, 0);
        guiGraphics.drawString(font, text, 0, 0, 0x00AAAA, false);

        String text2 = recipe.getMult() + "x Speed";
        poseStack.translate((font.width(text)-font.width(text2)) / 2.0, 10, 0);
        guiGraphics.drawString(font, text2, 0, 0, 0x00AAAA, false);

        poseStack.popPose();
    }
}
