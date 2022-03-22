package website.skylorbeck.minecraft.iconicwands.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import website.skylorbeck.minecraft.iconicwands.Declarar;
import website.skylorbeck.minecraft.iconicwands.Iconicwands;
import website.skylorbeck.minecraft.iconicwands.config.Parts;
import website.skylorbeck.minecraft.iconicwands.items.IconicWand;
import website.skylorbeck.minecraft.iconicwands.screen.WandBenchScreenHandler;

public class WandPedestalEntity extends BlockEntity implements Inventory, NamedScreenHandlerFactory {
    private Text customName;
    private ScreenHandler handler;
    private boolean hide = false;
    protected DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public WandPedestalEntity(BlockPos pos, BlockState state) {
        super(Declarar.WAND_PEDESTAL_ENTITY, pos, state);
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
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
        Inventories.readNbt(nbt, inventory);
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).isOf(Items.BARRIER))
                inventory.set(i, ItemStack.EMPTY);
        }
        this.setHide(nbt.getBoolean("hide"));
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
        nbt.putBoolean("hide",this.isHide());
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

    public static <E extends BlockEntity> void tick(World world, BlockPos blockPos, BlockState blockState, WandPedestalEntity pedestalEntity) {
        if (!world.isClient) {
            if (blockState.isOf(Declarar.WAND_PEDESTAL_DISPLAY))
            if (world.getTime() % 20 == 0) {
                ItemStack itemStack = new ItemStack(Declarar.ICONIC_WAND);
                IconicWand.saveComponents(itemStack,
                        new Parts.WandCluster(
                                Iconicwands.parts.tips.get(world.random.nextInt(Iconicwands.parts.tips.size())),
                                Iconicwands.parts.cores.get(world.random.nextInt(Iconicwands.parts.cores.size())),
                                Iconicwands.parts.handles.get(world.random.nextInt(Iconicwands.parts.handles.size()))
                        ));
                pedestalEntity.setStack(0,itemStack);
                pedestalEntity.markDirty();
                Packet<ClientPlayPacketListener> updatePacket = pedestalEntity.toUpdatePacket();

                world.getPlayers().forEach(player -> {
                    if(updatePacket != null )
                    ((ServerPlayerEntity) player).networkHandler.sendPacket(updatePacket);
                });
            }
        }
    }
}


