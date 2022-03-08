package website.skylorbeck.minecraft.iconicwands;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import website.skylorbeck.minecraft.iconicwands.config.Parts;
import website.skylorbeck.minecraft.iconicwands.entity.MagicProjectileEntity;
import website.skylorbeck.minecraft.iconicwands.items.IconicWand;

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
}
