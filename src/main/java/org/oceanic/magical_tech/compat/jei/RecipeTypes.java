package org.oceanic.magical_tech.compat.jei;

import mezz.jei.api.recipe.RecipeType;
import org.oceanic.magical_tech.MagicalTech;

public class RecipeTypes {
    public static final RecipeType<SoulBurningRecipe> SOUL_BURNING =
            mezz.jei.api.recipe.RecipeType.create(MagicalTech.MOD_ID, "soul_burning", SoulBurningRecipe.class);
}
