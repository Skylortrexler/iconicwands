package website.skylorbeck.minecraft.iconicwands;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import website.skylorbeck.minecraft.iconicwands.blocks.WandBench;
import website.skylorbeck.minecraft.iconicwands.config.Parts;
import website.skylorbeck.minecraft.iconicwands.entity.MagicProjectileEntity;
import website.skylorbeck.minecraft.iconicwands.items.IconicWand;
import website.skylorbeck.minecraft.iconicwands.screen.WandBenchScreenHandler;

public class Declarar {
//    public static final ItemGroup ICONIC_WAND_GROUP = FabricItemGroupBuilder.build(Iconicwands.getId("category"), () -> new ItemStack(Declarar.ICONIC_WAND));
    public static Identifier MAGIC_PROJECTILE_ENTITY_ID = Iconicwands.getId("magic");

    public static <T extends Entity> EntityType<T> createMagicEntityType(EntityType.EntityFactory<T> factory){
        return EntityType.Builder.create(factory,SpawnGroup.MISC)
                .setDimensions(0.5f,0.5f)
                .build(MAGIC_PROJECTILE_ENTITY_ID.toString());
    }
    public static final EntityType<MagicProjectileEntity> MAGIC_PROJECTILE = Registry.register(Registry.ENTITY_TYPE, MAGIC_PROJECTILE_ENTITY_ID,createMagicEntityType(MagicProjectileEntity::new));

    public static final Item ICONIC_WAND = new IconicWand(new FabricItemSettings().rarity(Rarity.EPIC).maxCount(1).maxDamage(1024).customDamage((stack, amount, entity, breakCallback) -> {
        IconicWand wand =  ((IconicWand)stack.getItem());
        String modelData = wand.getPartCombo(stack);
        Parts.Tip tip = Iconicwands.parts.tips.get(Integer.parseInt(modelData.substring(1,3)));
        Parts.Core core = Iconicwands.parts.cores.get(Integer.parseInt(modelData.substring(3,5)));
        Parts.Handle handle = Iconicwands.parts.handles.get(Integer.parseInt(modelData.substring(5,7)));
        return handle.getManaCost()+tip.getManaCost();
    }));

    public static final Block WAND_BENCH = new WandBench(FabricBlockSettings.copy(Blocks.CRAFTING_TABLE));
    public static final BlockItem WAND_BENCH_ITEM = new BlockItem(WAND_BENCH, new FabricItemSettings());

    public static ScreenHandlerType<WandBenchScreenHandler> WANDING;
}
