/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package website.skylorbeck.minecraft.iconicwands.screen.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import website.skylorbeck.minecraft.iconicwands.entity.WandBenchEntity;

public class WandCraftingResultSlot
extends Slot {
    private final Inventory input;
    private final PlayerEntity player;
    private int amount;

    public WandCraftingResultSlot(PlayerEntity player, Inventory input, int index, int x, int y) {
        super(input, index, x, y);
        this.player = player;
        this.input = input;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack takeStack(int amount) {
        if (this.hasStack()) {
            this.amount += Math.min(amount, this.getStack().getCount());
        }
        return super.takeStack(amount);
    }

    @Override
    protected void onCrafted(ItemStack stack, int amount) {
        this.amount += amount;
        this.onCrafted(stack);
    }

    @Override
    protected void onTake(int amount) {
        this.amount += amount;
    }

    @Override
    protected void onCrafted(ItemStack stack) {
        if (this.amount > 0) {
            stack.onCraft(this.player.world, this.player, this.amount);
        }
        if (this.inventory instanceof RecipeUnlocker) {
            ((RecipeUnlocker)((Object)this.inventory)).unlockLastRecipe(this.player);
        }
        this.amount = 0;
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        this.onCrafted(stack);
        for (int i = 1; i < 4; ++i) {
            ItemStack itemStack = input.getStack(i);
            if (!itemStack.isEmpty()) {
                input.removeStack(i, 1);
            }
            if (input instanceof WandBenchEntity wandBench) {
                wandBench.getHandler().onContentChanged(input);
                Packet<ClientPlayPacketListener> updatePacket = wandBench.toUpdatePacket();
                if (updatePacket != null && !player.world.isClient) {
                    ((ServerPlayerEntity)player).networkHandler.sendPacket(updatePacket);
                }
            }
        }
    }
}

