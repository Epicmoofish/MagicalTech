package org.oceanic.magical_tech.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.oceanic.magical_tech.MagicalTech;
import org.oceanic.magical_tech.soul_burning.SoulBurningMap;

import java.util.LinkedList;
import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    private static final ResourceLocation UID = new ResourceLocation(MagicalTech.MOD_ID, "jei_plugin");
    public static final RecipeType<SoulBurningRecipe> SOUL_BURNING_TYPE = RecipeType.create(MagicalTech.MOD_ID, "soul_burning", SoulBurningRecipe.class);
    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SoulBurningCategory(registration.getJeiHelpers().getGuiHelper())) ;
    }
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(MagicalTech.CRUDE_SOULIUM_GENERATOR), SOUL_BURNING_TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<SoulBurningRecipe> entries = new LinkedList<>();
        for (Item key: SoulBurningMap.getKeys()) {
            try {
                long val = SoulBurningMap.get(key);
                entries.add(new SoulBurningRecipe(key, val));
            } catch (Exception e) {
                MagicalTech.LOGGER.error("Failed to get key: " + key);
            }
        }
        entries.sort((o1, o2) -> {
            long val = o2.burnAmount() - o1.burnAmount();
            if (val == 0) return 0;
            if (val > 0) return 1;
            return -1;
        });
        registration.addRecipes(SOUL_BURNING_TYPE, entries);
    }
}
