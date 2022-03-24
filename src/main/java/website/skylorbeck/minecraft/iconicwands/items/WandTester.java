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

public class WandTester extends Item {
    public WandTester(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        for (int i = 0; i < Iconicwands.parts.getAllTips().size(); i++) {
            for (int j = 0; j < Iconicwands.parts.getAllCores().size(); j++) {
                for (int k = 0; k < Iconicwands.parts.getAllHandles().size(); k++) {
                    BlockPos pos = user.getBlockPos().offset(user.getHorizontalFacing(),2).add(i*2,j*2,k*2);
                    ItemStack wand = new ItemStack(Declarar.ICONIC_WAND);
                    IconicWand.saveComponents(wand, Iconicwands.parts.getAllTips().get(i), Iconicwands.parts.getAllCores().get(j), Iconicwands.parts.getAllHandles().get(k));
                    world.setBlockState(pos,Declarar.WAND_PEDESTAL.getDefaultState());
                    ((WandPedestalEntity)world.getBlockEntity(pos)).setStack(0,wand);
//                    ((WandPedestalEntity)world.getBlockEntity(pos)).setHide(true);//disabled render movement, 10-20% performance increase
                }
            }
        }
        user.sendMessage(Text.of((Iconicwands.parts.getAllTips().size()*Iconicwands.parts.getAllCores().size()*Iconicwands.parts.getAllHandles().size())+" wands placed."),true);
        return super.use(world, user, hand);
    }
}
