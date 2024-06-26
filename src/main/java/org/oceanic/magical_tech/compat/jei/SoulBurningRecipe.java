package org.oceanic.magical_tech.compat.jei;

import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.item.Item;

public record SoulBurningRecipe(Item item, long burnAmount, long mult) implements IRecipeCategoryExtension<SoulBurningRecipe> {

    public Item getItem() {
        return item;
    }
    public long getAmount() {
        return burnAmount;
    }
    public long getMult() {
        return mult;
    }
}
