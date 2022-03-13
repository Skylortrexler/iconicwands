package website.skylorbeck.minecraft.iconicwands.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import website.skylorbeck.minecraft.iconicwands.Declarar;
import website.skylorbeck.minecraft.iconicwands.Iconicwands;
import website.skylorbeck.minecraft.iconicwands.entity.WandPedestalEntity;

public class WandPresetTester extends Item {
    public WandPresetTester(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        for (IconicWand.Presets wand : IconicWand.Presets.values()) {
            ItemStack stack = new ItemStack(Declarar.ICONIC_WAND);
            IconicWand.saveComponents(stack,wand.getWand());
            user.getInventory().offerOrDrop(stack);
        }
        user.sendMessage(Text.of(IconicWand.Presets.values().length+" Wands have been placed in your inventory."),false);
        return super.use(world, user, hand);
    }
}
