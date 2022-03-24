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
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
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


public class IconicWand extends RangedWeaponItem{

    public IconicWand(Settings settings) {
        super(settings);
    }

    public static void saveComponents(ItemStack stack, Parts.WandCluster wandCluster) {
        saveComponents(stack, wandCluster.getTip(), wandCluster.getCore(), wandCluster.getHandle());
    }

    public static void saveComponents(ItemStack stack,Parts.Tip tip, Parts.Core core, Parts.Handle handle) {
        saveComponents(stack, core, handle, tip);
    }
    public static void saveComponents(ItemStack stack, Parts.Core core, Parts.Handle handle, Parts.Tip tip) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt("CustomModelData",partsToInt(tip, core, handle));
    }

    public static int partsToInt(Parts.Tip tip, Parts.Core core, Parts.Handle handle) {
        int tipInt = Iconicwands.parts.getAllTips().indexOf(tip);
        int coreInt = Iconicwands.parts.getAllCores().indexOf(core);
        int handleInt = Iconicwands.parts.getAllHandles().indexOf(handle);
        int shiftedParts = (tipInt & 0xFF)<< 16 | (coreInt & 0xFF) << 8 | (handleInt & 0xFF);
        return shiftedParts;
    }
    public static Parts.WandCluster intToParts(int shiftedParts) {
        int tipInt = shiftedParts >> 16 & 0xFF;
        int coreInt = shiftedParts >> 8 & 0xFF;
        int handleInt = shiftedParts & 0xFF;
        return new Parts.WandCluster(Iconicwands.parts.getAllTips().get(tipInt), Iconicwands.parts.getAllCores().get(coreInt), Iconicwands.parts.getAllHandles().get(handleInt));
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
            Parts.WandCluster wand = IconicWand.getPartComobo(stack);
            tooltip.add(new TranslatableText("item.iconicwands.damage").append(": " + wand.getHandle().getDamage()));
            tooltip.add(new TranslatableText("item.iconicwands.mana_cost").append(": " + (wand.getTip().getManaCost() + wand.getHandle().getManaCost())));
            tooltip.add(new TranslatableText("item.iconicwands.firerate").append(": " + (wand.getCore().getFirerate() + wand.getHandle().getFirerate())));
            if (context.isAdvanced() || Screen.hasShiftDown()) {
                tooltip.add(new TranslatableText("item.iconicwands.crit").append(": " + 100*(wand.getTip().getCriticalChance()+wand.getHandle().getCriticalChance())));
                tooltip.add(new TranslatableText("item.iconicwands.recharge_amount").append(": " + (wand.getTip().getRechargeAmount() + wand.getCore().getRechargeAmount())));
                tooltip.add(new TranslatableText("item.iconicwands.recharge_rate").append(": " + (wand.getCore().getRechargeRate())));
                tooltip.add(new TranslatableText("item.iconicwands.recharge_delay").append(": " + (wand.getCore().getRechargeDelay())));
                tooltip.add(new TranslatableText("item.iconicwands.range").append(": " + (wand.getCore().getRange())));
                tooltip.add(new TranslatableText("item.iconicwands.speed").append(": " + (wand.getTip().getSpeed())));
                tooltip.add(new TranslatableText("item.iconicwands.divergence").append(": " + (wand.getTip().getDivergence())));
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
            } else {
                if (wand.getCore().getRechargeRate()==0 || world.getTime() % (wand.getCore().getRechargeRate()* 20L) == 0) {
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
            int wandInt = getPartIntCombo(stack);
            Parts.WandCluster wand = intToParts(wandInt);
            boolean crit = world.random.nextFloat() <= wand.getTip().getCriticalChance() + wand.getHandle().getCriticalChance();

            int k;
            int j;
            int projectileCount = wandInt == Iconicwands.Presets.boomstick.getWandInt()?6:wandInt == Iconicwands.Presets.kynan.getWandInt()?world.random.nextInt(3)+1: 1;
            for (int i = 0; i < projectileCount; i++) {
                MagicProjectileEntity persistentProjectileEntity = new MagicProjectileEntity(world, playerEntity);
                persistentProjectileEntity.setOwner(playerEntity);
                persistentProjectileEntity.setNoGravity(true);
                persistentProjectileEntity.setMaxDist(wand.getCore().getRange());
                persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, wand.getTip().getSpeed(), wand.getTip().getDivergence()*2);
                persistentProjectileEntity.setColor(Color.ofRGB(wand.getTip().getRed(), wand.getCore().getGreen(), wand.getHandle().getBlue()).getColor());
                persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
                persistentProjectileEntity.setDamage(wand.getHandle().getDamage());
                if (wandInt == (Iconicwands.Presets.overworld.getWandInt())) {
                    persistentProjectileEntity.setDoesLight(true);
                } else if (wandInt == (Iconicwands.Presets.nether.getWandInt())) {
                    persistentProjectileEntity.setOnFireFor(100);
                    persistentProjectileEntity.setDoesBurn(true);
                } else if (wandInt == (Iconicwands.Presets.end.getWandInt())) {
                    if (playerEntity.isSneaking()) {
                        persistentProjectileEntity.setDoesWarp(true);
                    }
                } else if (wandInt == (Iconicwands.Presets.food.getWandInt())) {
                    playerEntity.eatFood(world, new ItemStack(Items.POTATO));
                } else if (wandInt == (Iconicwands.Presets.forest.getWandInt())) {
                    world.playSoundFromEntity(null, playerEntity, SoundEvents.ENTITY_PARROT_AMBIENT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                } else if (wandInt == (Iconicwands.Presets.magus.getWandInt()) || (wandInt == Iconicwands.Presets.kynan.getWandInt() && crit)) {
                    persistentProjectileEntity.setDoesExplode(true);
                } else if (wandInt == (Iconicwands.Presets.scarlet.getWandInt()) && crit) {
                    persistentProjectileEntity.setDoesLightning(true);
                }
                    persistentProjectileEntity.setCritical(crit);

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
                world.spawnEntity(persistentProjectileEntity);
            }

            world.playSoundFromEntity(null, playerEntity, SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 0.5f + world.random.nextFloat(0.5f), (crit ? 2f : 0.25f) + world.random.nextFloat(0.75f));
            stack.getOrCreateNbt().putInt("recharge_time", wand.getCore().getRechargeDelay());
            stack.getOrCreateNbt().putInt("recharge_delay", 0);

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

    @Override
    public Text getName(ItemStack stack) {
        int wand = IconicWand.getPartIntCombo(stack);
        if (wand==(Iconicwands.Presets.overworld.getWandInt())){
            return new TranslatableText("item.iconicwands.overworld_wand");
        } else
        if (wand==(Iconicwands.Presets.nether.getWandInt())){
            return new TranslatableText("item.iconicwands.nether_wand");
        } else
        if (wand==(Iconicwands.Presets.end.getWandInt())){
            return new TranslatableText("item.iconicwands.end_wand");
        } else
        if (wand==(Iconicwands.Presets.food.getWandInt())){
            return new TranslatableText("item.iconicwands.food_wand");
        } else
        if (wand==(Iconicwands.Presets.forest.getWandInt())){
            return new TranslatableText("item.iconicwands.forest_wand");
        } else
        if (wand==(Iconicwands.Presets.magus.getWandInt())){
            return new TranslatableText("item.iconicwands.magus_wand");
        } else
        if (wand==(Iconicwands.Presets.kynan.getWandInt())){
            return new TranslatableText("item.iconicwands.kynan_wand");
        } else
        if (wand==(Iconicwands.Presets.scarlet.getWandInt())){
            return new TranslatableText("item.iconicwands.scarlet_wand");
        } else
        if (wand==(Iconicwands.Presets.boomstick.getWandInt())){
            return new TranslatableText("item.iconicwands.boomstick_wand");
        } else

        return super.getName(stack);
    }
}