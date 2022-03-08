package website.skylorbeck.minecraft.iconicwands.items;

import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class WandTooltipData implements TooltipData {
    private final DefaultedList<ItemStack> inventory;
    public WandTooltipData(DefaultedList<ItemStack> inventory) {
        this.inventory = inventory;
    }
    public DefaultedList<ItemStack> getInventory() {
        return this.inventory;
    }

}
