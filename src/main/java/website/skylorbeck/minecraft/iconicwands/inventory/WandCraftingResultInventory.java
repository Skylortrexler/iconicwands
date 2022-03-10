/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package website.skylorbeck.minecraft.iconicwands.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

public class WandCraftingResultInventory
implements Inventory,
RecipeUnlocker {
    private final DefaultedList<ItemStack> stacks = DefaultedList.ofSize(1, ItemStack.EMPTY);
    @Nullable
    private Recipe<?> lastRecipe;

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.stacks) {
            if (itemStack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.stacks.get(4);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.removeStack(this.stacks, 4);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.stacks, 4);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.stacks.set(4, stack);
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        this.stacks.clear();
    }

    @Override
    public void setLastRecipe(@Nullable Recipe<?> recipe) {
        this.lastRecipe = recipe;
    }

    @Override
    @Nullable
    public Recipe<?> getLastRecipe() {
        return this.lastRecipe;
    }
}

