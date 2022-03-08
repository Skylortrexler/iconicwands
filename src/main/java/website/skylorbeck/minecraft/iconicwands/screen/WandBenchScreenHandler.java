/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package website.skylorbeck.minecraft.iconicwands.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import website.skylorbeck.minecraft.iconicwands.Declarar;
import website.skylorbeck.minecraft.iconicwands.Iconicwands;
import website.skylorbeck.minecraft.iconicwands.config.Parts;
import website.skylorbeck.minecraft.iconicwands.items.IconicWand;

import java.util.concurrent.atomic.AtomicBoolean;

public class WandBenchScreenHandler
extends AbstractRecipeScreenHandler<CraftingInventory> {
    private final CraftingInventory input = new CraftingInventory(this, 3,1);
    private final CraftingResultInventory result = new CraftingResultInventory();
    private final ScreenHandlerContext context;
    private final PlayerEntity player;

    public WandBenchScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public WandBenchScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Declarar.WANDING, syncId);
        int j;
        int i;
        this.context = context;
        this.player = playerInventory.player;
        this.addSlot(new CraftingResultSlot(playerInventory.player, this.input, this.result, 0, 124, 35){
            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                this.onCrafted(stack);
                for (int i = 0; i < 3; ++i) {
                    ItemStack itemStack = input.getStack(i);
                    if (!itemStack.isEmpty()) {
                        input.removeStack(i, 1);
                    }

                }
            }
        });
        for (i = 0; i < 3; ++i) {
            int finalI = i;
            this.addSlot(new Slot(this.input, finalI, 30 + i * 18, 17){
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        AtomicBoolean bl = new AtomicBoolean(false);
                        switch (finalI) {
                            case 0 -> Iconicwands.parts.tips.forEach(tip -> {
                                if (tip.getIdentifier().equals(Registry.ITEM.getId(stack.getItem()).toString()))
                                    bl.set(true);
                            });
                            case 1 -> Iconicwands.parts.cores.forEach(core -> {
                                if (core.getIdentifier().equals(Registry.ITEM.getId(stack.getItem()).toString()))
                                    bl.set(true);
                            });
                            case 2 -> Iconicwands.parts.handles.forEach(handle -> {
                                if (handle.getIdentifier().equals(Registry.ITEM.getId(stack.getItem()).toString()))
                                    bl.set(true);
                            });
                        }
                        return super.canInsert(stack) && bl.get();
                    }
                });
        }
        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    protected static void updateResult(ScreenHandler handler, World world, PlayerEntity player, CraftingInventory craftingInventory, CraftingResultInventory resultInventory) {
        if (world.isClient) {
            return;
        }
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
        ItemStack result = ItemStack.EMPTY;
        Parts.Tip tipResult = null;
        Parts.Core coreResult = null;
        Parts.Handle handleResult = null;
        //todo wand crafting logic goes here
        for (int i = 0; i < craftingInventory.size() ; i++) {
            if (craftingInventory.getStack(i).isEmpty()){
                continue;
            }
            ItemStack ingredient = craftingInventory.getStack(i);
            Identifier identifier = Registry.ITEM.getId(ingredient.getItem());
            switch (i) {
                case 0:
                    for (Parts.Tip tip : Iconicwands.parts.tips) {
                        if (tip.getIdentifier().equals(identifier.toString())) {
                            tipResult = tip;
                        }
                    }
                    break;
                case 1:
                    for (Parts.Core core : Iconicwands.parts.cores) {
                        if (core.getIdentifier().equals(identifier.toString())) {
                            coreResult = core;
                        }
                    }
                    break;
                case 2:
                    for (Parts.Handle handle : Iconicwands.parts.handles) {
                        if (handle.getIdentifier().equals(identifier.toString())) {
                            handleResult = handle;
                        }
                    }
                    break;
            }
        }
        if (tipResult != null && coreResult != null && handleResult != null) {
            result = new ItemStack(Declarar.ICONIC_WAND);
            IconicWand.saveComponents(result, coreResult, handleResult, tipResult);
        }
        resultInventory.setStack(0, result);
        handler.setPreviousTrackedSlot(0, result);
        serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, result));
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        this.context.run((world, pos) -> WandBenchScreenHandler.updateResult(this, world, this.player, this.input, this.result));
    }

    @Override
    public void populateRecipeFinder(RecipeMatcher finder) {
        this.input.provideRecipeInputs(finder);
    }

    @Override
    public void clearCraftingSlots() {
        this.input.clear();
        this.result.clear();
    }

    @Override
    public boolean matches(Recipe<? super CraftingInventory> recipe) {
        return false;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.input));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return WandBenchScreenHandler.canUse(this.context, player, Declarar.WAND_BENCH);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index == 0) {
                this.context.run((world, pos) -> itemStack2.getItem().onCraft(itemStack2, (World)world, player));
                if (!this.insertItem(itemStack2, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
                //todo fix this vVv
            } else if (index >= 4 && index < 40 ? !this.insertItem(itemStack2, 1, 4, false) && (index < 31 ? !this.insertItem(itemStack2, 31, 40, false) : !this.insertItem(itemStack2, 4, 31, false)) : !this.insertItem(itemStack2, 4, 40, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, itemStack2);
            if (index == 0) {
                player.dropItem(itemStack2, false);
            }
        }
        return itemStack;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.result && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return 0;
    }

    @Override
    public int getCraftingWidth() {
        return this.input.getWidth();
    }

    @Override
    public int getCraftingHeight() {
        return this.input.getHeight();
    }

    @Override
    public int getCraftingSlotCount() {
        return 4;
    }

    @Override
    public RecipeBookCategory getCategory() {
        return RecipeBookCategory.CRAFTING;
    }

    @Override
    public boolean canInsertIntoSlot(int index) {
        return index != this.getCraftingResultSlotIndex();
    }
}

