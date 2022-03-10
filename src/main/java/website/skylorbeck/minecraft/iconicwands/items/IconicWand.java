package website.skylorbeck.minecraft.iconicwands.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import website.skylorbeck.minecraft.iconicwands.Color;
import website.skylorbeck.minecraft.iconicwands.Iconicwands;
import website.skylorbeck.minecraft.iconicwands.config.Parts;
import website.skylorbeck.minecraft.iconicwands.entity.MagicProjectileEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;


public class IconicWand extends RangedWeaponItem{

    public IconicWand(Settings settings) {
        super(settings);
    }

    public static void saveComponents(ItemStack stack, Parts.Core core, Parts.Handle handle, Parts.Tip tip) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt("CustomModelData",partsToInt(tip, core, handle));
    }

    public static int partsToInt(Parts.Tip tip, Parts.Core core, Parts.Handle handle) {
        int tipInt = Iconicwands.parts.tips.indexOf(tip);
        int coreInt = Iconicwands.parts.cores.indexOf(core);
        int handleInt = Iconicwands.parts.handles.indexOf(handle);
        return (tipInt & 0xFF)<< 16 | (coreInt & 0xFF) << 8 | (handleInt & 0xFF);
    }
    public static Parts.WandCluster intToParts(int shiftedParts) {
        int tipInt = shiftedParts >> 16 & 0xFF;
        int coreInt = shiftedParts >> 8 & 0xFF;
        int handleInt = shiftedParts & 0xFF;
        return new Parts.WandCluster(Iconicwands.parts.tips.get(tipInt), Iconicwands.parts.cores.get(coreInt), Iconicwands.parts.handles.get(handleInt));
    }
    public static int getPartIntCombo(ItemStack stack){
        return stack.getOrCreateNbt().getInt("CustomModelData");
    }
    public static Parts.WandCluster getPartComobo(ItemStack stack){
        return intToParts(getPartIntCombo(stack));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.getOrCreateNbt().getInt("CustomModelData") >= 0) {
            if (context.isAdvanced() || Screen.hasShiftDown()) {
                Parts.WandCluster wand = IconicWand.getPartComobo(stack);
                tooltip.add(new TranslatableText("item.iconicwands.damage").append(": " + wand.getHandle().getDamage()));
                tooltip.add(new TranslatableText("item.iconicwands.crit").append(": " + 100*(wand.getTip().getCriticalChance()+wand.getHandle().getCriticalChance())));
                tooltip.add(new TranslatableText("item.iconicwands.firerate").append(": " + (wand.getCore().getFirerate() + wand.getHandle().getFirerate())));
                tooltip.add(new TranslatableText("item.iconicwands.mana_cost").append(": " + (wand.getTip().getManaCost() + wand.getHandle().getManaCost())));
                tooltip.add(new TranslatableText("item.iconicwands.recharge_amount").append(": " + (wand.getTip().getRechargeAmount() + wand.getCore().getRechargeAmount())));
                tooltip.add(new TranslatableText("item.iconicwands.recharge_rate").append(": " + (wand.getCore().getRechargeRate())));
                tooltip.add(new TranslatableText("item.iconicwands.recharge_delay").append(": " + (wand.getCore().getRechargeDelay())));
                tooltip.add(new TranslatableText("item.iconicwands.range").append(": " + (wand.getCore().getRange())));
                tooltip.add(new TranslatableText("item.iconicwands.speed").append(": " + (wand.getTip().getSpeed())));
            } else {
                tooltip.add(new TranslatableText("item.iconicwands.advanced_tooltip"));
            }
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        DefaultedList<ItemStack> defaultedList = DefaultedList.of();
        if (stack.getOrCreateNbt().getInt("CustomModelData") >=0) {
            Parts.WandCluster wand = IconicWand.getPartComobo(stack);
            defaultedList.add(new ItemStack(Registry.ITEM.get(new Identifier(wand.getTip().getIdentifier()))));
            defaultedList.add(new ItemStack(Registry.ITEM.get(new Identifier(wand.getCore().getIdentifier()))));
            defaultedList.add(new ItemStack(Registry.ITEM.get(new Identifier(wand.getHandle().getIdentifier()))));
            return Optional.of(new WandTooltipData(defaultedList));
        }
        return Optional.empty();
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient) return;
        Parts.WandCluster wand = IconicWand.getPartComobo(stack);
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        int rechargeTime = nbtCompound.getInt("recharge_time");
//        int rechargeDelay = nbtCompound.getInt("recharge_delay");
        if (stack.isDamaged()) {
            if (rechargeTime > 0 ) {
                if (world.getTime() % 20 == 0)
                    rechargeTime--;
            }else {
                if (world.getTime() % (wand.getCore().getRechargeRate()* 20L) == 0) {
//                if (++rechargeDelay % wand.getCore().getRechargeRate() == 0) {
                    stack.setDamage(stack.getDamage() - (wand.getCore().getRechargeAmount() + wand.getTip().getRechargeAmount()));
//                    rechargeDelay = 0;
                }
            }
        } else if (rechargeTime < wand.getCore().getRechargeDelay()) {
            rechargeTime = wand.getCore().getRechargeDelay();
//            rechargeDelay = 0;
        }
        if (nbtCompound.getInt("recharge_time") != rechargeTime)
            nbtCompound.putInt("recharge_time", rechargeTime);
//        if (nbtCompound.getInt("recharge_delay") != rechargeDelay)
//            nbtCompound.putInt("recharge_delay", rechargeDelay);

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public boolean isUsedOnRelease(ItemStack stack) {
        return true;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity playerEntity, int remainingUseTicks) {
        if (!world.isClient) {
            Parts.WandCluster wand = IconicWand.getPartComobo(stack);

            int k;
            int j;
            MagicProjectileEntity persistentProjectileEntity = new MagicProjectileEntity(world, playerEntity);
            persistentProjectileEntity.setNoGravity(true);
            persistentProjectileEntity.setMaxDist(wand.getCore().getRange());
//            persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, 0, wand.getTip().getDivergence());
            persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, wand.getTip().getSpeed(), wand.getTip().getDivergence());
            if (world.random.nextFloat() <= wand.getTip().getCriticalChance()+wand.getHandle().getCriticalChance()) {
                persistentProjectileEntity.setCritical(true);
            }

            persistentProjectileEntity.setColor(Color.ofRGB(wand.getTip().getRed(),wand.getCore().getGreen(),wand.getHandle().getBlue()).getColor());

            if ((j = EnchantmentHelper.getLevel(Enchantments.POWER, stack)) > 0) {
                persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() + (double) j * 0.5 + 0.5);
            }
            if ((k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack)) > 0) {
                persistentProjectileEntity.setPunch(k);
            }
            if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                persistentProjectileEntity.setOnFireFor(100);
            }
            stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(playerEntity.getActiveHand()));
            persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
            world.spawnEntity(persistentProjectileEntity);
            world.playSoundFromEntity(null,playerEntity, SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.PLAYERS,0.5f+world.random.nextFloat(0.5f),(persistentProjectileEntity.isCritical()?2f:0.25f)+world.random.nextFloat(0.75f));
//            rechargeTime = wand.getCore().getRechargeDelay();
            stack.getOrCreateNbt().putInt("recharge_time", wand.getCore().getRechargeDelay());
            stack.getOrCreateNbt().putInt("recharge_delay",0);

            if (playerEntity instanceof PlayerEntity player) {
                player.getItemCooldownManager().set(this, wand.getCore().getFirerate() + wand.getHandle().getFirerate());
            }
        }
        super.onStoppedUsing(stack, world, playerEntity, remainingUseTicks);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return BOW_PROJECTILES;
    }

    @Override
    public int getRange() {
        return 15;
    }

}