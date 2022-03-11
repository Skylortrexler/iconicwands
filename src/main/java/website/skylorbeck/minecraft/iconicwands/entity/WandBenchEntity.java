package website.skylorbeck.minecraft.iconicwands.entity;

import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import website.skylorbeck.minecraft.iconicwands.Declarar;
import website.skylorbeck.minecraft.iconicwands.screen.WandBenchScreenHandler;
import website.skylorbeck.minecraft.skylorlib.SkylorLib;
import website.skylorbeck.minecraft.skylorlib.storage.StorageUtils;

import java.util.Set;

public class WandBenchEntity extends BlockEntity implements Inventory, NamedScreenHandlerFactory {
    private Text customName;
    private ScreenHandler handler;

    protected DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

    public WandBenchEntity(BlockPos pos, BlockState state) {
        super(Declarar.WAND_BENCH_ENTITY, pos, state);
    }

    public DefaultedList<ItemStack> getInventory() {
        return inventory;
    }

    public ScreenHandler getHandler() {
        return handler;
    }

    public void setHandler(ScreenHandler handler) {
        this.handler = handler;
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.inventory) {
            if (itemStack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        if (slot < 0 || slot >= this.inventory.size()) {
            return ItemStack.EMPTY;
        }
        return this.inventory.get(slot);
    }


    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemStack = Inventories.splitStack(this.inventory, slot, amount);
        if (!itemStack.isEmpty()) {
            this.markDirty();
        }
        return itemStack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack itemStack = this.inventory.get(slot);
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        this.inventory.set(slot, ItemStack.EMPTY);
        return itemStack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }
        this.markDirty();
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        inventory.clear();
    }
    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new WandBenchScreenHandler(syncId, inv, this);
    }

    @Override
    public Text getDisplayName() {
        return this.customName != null ? this.customName : new TranslatableText("container.iconicwands.wand_bench");
    }

    public boolean hasCustomName(){
        return this.customName != null;
    }

    public void setCustomName(@Nullable Text customName) {
        this.customName = customName;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt,inventory);
        super.readNbt(nbt);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        NbtList nbtList = new NbtList();
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack = inventory.get(i);
            if (itemStack.isEmpty()){
                itemStack = Items.BARRIER.getDefaultStack();
            };
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)i);
            itemStack.writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }
        if (!nbtList.isEmpty()) {
            nbt.put("Items", nbtList);
        }
        super.writeNbt(nbt);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
