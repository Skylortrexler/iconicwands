package website.skylorbeck.minecraft.iconicwands;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import vazkii.patchouli.api.PatchouliAPI;
import website.skylorbeck.minecraft.iconicwands.blocks.WandPedestal;
import website.skylorbeck.minecraft.iconicwands.blocks.WeakLightBlock;
import website.skylorbeck.minecraft.iconicwands.blocks.WandBench;
import website.skylorbeck.minecraft.iconicwands.config.Parts;
import website.skylorbeck.minecraft.iconicwands.entity.MagicProjectileEntity;
import website.skylorbeck.minecraft.iconicwands.entity.WandBenchEntity;
import website.skylorbeck.minecraft.iconicwands.entity.WandPedestalEntity;
import website.skylorbeck.minecraft.iconicwands.items.IconicWand;
import website.skylorbeck.minecraft.iconicwands.items.WandTester;
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
        Parts.WandCluster wand = IconicWand.getPartComobo(stack);
        return wand.getHandle().getManaCost()+wand.getTip().getManaCost();
    }));

    public static final Block WAND_BENCH = new WandBench(FabricBlockSettings.copy(Blocks.CRAFTING_TABLE));

    public static final BlockEntityType<WandBenchEntity> WAND_BENCH_ENTITY =Registry.register(
            Registry.BLOCK_ENTITY_TYPE, Iconicwands.getId("wand_bench_entity"),
            FabricBlockEntityTypeBuilder.create(WandBenchEntity::new, WAND_BENCH).build(null));

    public static final BlockItem WAND_BENCH_ITEM = new BlockItem(WAND_BENCH, new FabricItemSettings().group(ItemGroup.MISC)){
        @Override
        public void onCraft(ItemStack stack, World world, PlayerEntity player) {
            ItemStack book = PatchouliAPI.get().getBookStack(Iconicwands.getId("book_1"));
            if (!player.getInventory().contains(book))
            player.getInventory().offerOrDrop(book);
            super.onCraft(stack, world, player);
        }
    };
    public static final Block WAND_PEDESTAL = new WandPedestal(FabricBlockSettings.copy(Blocks.BLACKSTONE));

    public static final BlockEntityType<WandPedestalEntity> WAND_PEDESTAL_ENTITY =Registry.register(
            Registry.BLOCK_ENTITY_TYPE, Iconicwands.getId("wand_pedestal_entity"),
            FabricBlockEntityTypeBuilder.create(WandPedestalEntity::new, WAND_PEDESTAL).build(null));

    public static final BlockItem WAND_PEDESTAL_ITEM = new BlockItem(WAND_PEDESTAL, new FabricItemSettings().group(ItemGroup.MISC));

    public static Block TIMED_LIGHT = new WeakLightBlock(FabricBlockSettings.copy(Blocks.LIGHT));

    public static Item WAND_TESTER = new WandTester(new Item.Settings());

    public static ScreenHandlerType<WandBenchScreenHandler> WANDING;
}
