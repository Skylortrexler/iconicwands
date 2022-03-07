package website.skylorbeck.minecraft.iconicwands;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.CustomDamageHandler;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;
import website.skylorbeck.minecraft.iconicwands.items.IconicWand;

import java.util.function.Consumer;

public class Declarar {
    public static final ItemGroup ICONIC_WAND_GROUP = FabricItemGroupBuilder.build(Iconicwands.getId("category"), () -> new ItemStack(Declarar.ICONIC_WAND));
    public static final Item ICONIC_WAND = new IconicWand(new FabricItemSettings().group(ICONIC_WAND_GROUP).rarity(Rarity.EPIC).maxCount(1).maxDamage(64).customDamage(new CustomDamageHandler() {
        @Override
        public int damage(ItemStack stack, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback) {
            return entity.getRandom().nextInt(10);//TODO: Implement custom damage logic that turns damage into mana
        }
    }));
}
