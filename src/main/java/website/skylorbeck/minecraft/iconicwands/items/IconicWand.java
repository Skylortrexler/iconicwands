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


public class IconicWand extends RangedWeaponItem{

    public IconicWand(Settings settings) {
        super(settings);
    }

    public static void saveComponents(ItemStack stack, Parts.Core core, Parts.Handle handle, Parts.Tip tip) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt("CustomModelData", Integer.parseInt(1 + String.format("%02d", Iconicwands.parts.tips.indexOf(tip)) + String.format("%02d", Iconicwands.parts.cores.indexOf(core)) + String.format("%02d", Iconicwands.parts.handles.indexOf(handle))));
//        Logger.getGlobal().log(Level.SEVERE, nbt.getInt("CustomModelData") + "");
    }
    public static String getPartCombo(ItemStack stack) {
        String modelData = String.valueOf(stack.getOrCreateNbt().getInt("CustomModelData"));
        if (modelData.length() <= 6) {
            modelData = "1000000";
        }
        return modelData;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.getOrCreateNbt().getInt("CustomModelData") >=1000000) {
            if (context.isAdvanced() || Screen.hasShiftDown()) {
                String modelData = getPartCombo(stack);
                Parts.Tip tip = Iconicwands.parts.tips.get(Integer.parseInt(modelData.substring(1, 3)));
                Parts.Core core = Iconicwands.parts.cores.get(Integer.parseInt(modelData.substring(3, 5)));
                Parts.Handle handle = Iconicwands.parts.handles.get(Integer.parseInt(modelData.substring(5, 7)));
                tooltip.add(new TranslatableText("item.iconicwands.damage").append(": " + handle.getDamage()));
                tooltip.add(new TranslatableText("item.iconicwands.firerate").append(": " + (core.getFirerate() + handle.getFirerate())));
                tooltip.add(new TranslatableText("item.iconicwands.mana_cost").append(": " + (tip.getManaCost() + handle.getManaCost())));
                tooltip.add(new TranslatableText("item.iconicwands.recharge_amount").append(": " + (tip.getRechargeAmount() + core.getRechargeAmount())));
                tooltip.add(new TranslatableText("item.iconicwands.recharge_rate").append(": " + (core.getRechargeRate())));
                tooltip.add(new TranslatableText("item.iconicwands.recharge_delay").append(": " + (core.getRechargeDelay())));
                tooltip.add(new TranslatableText("item.iconicwands.range").append(": " + (core.getRange())));
                tooltip.add(new TranslatableText("item.iconicwands.speed").append(": " + (tip.getSpeed())));
            } else {
                tooltip.add(new TranslatableText("item.iconicwands.advanced_tooltip"));
            }
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        DefaultedList<ItemStack> defaultedList = DefaultedList.of();
        if (stack.getOrCreateNbt().getInt("CustomModelData") >=1000000) {
            String modelData = getPartCombo(stack);
            Parts.Tip tip = Iconicwands.parts.tips.get(Integer.parseInt(modelData.substring(1, 3)));
            Parts.Core core = Iconicwands.parts.cores.get(Integer.parseInt(modelData.substring(3, 5)));
            Parts.Handle handle = Iconicwands.parts.handles.get(Integer.parseInt(modelData.substring(5, 7)));
            defaultedList.add(new ItemStack(Registry.ITEM.get(new Identifier(tip.getIdentifier()))));
            defaultedList.add(new ItemStack(Registry.ITEM.get(new Identifier(core.getIdentifier()))));
            defaultedList.add(new ItemStack(Registry.ITEM.get(new Identifier(handle.getIdentifier()))));
            return Optional.of(new WandTooltipData(defaultedList));
        }
        return Optional.empty();
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient) return;
        String modelData = getPartCombo(stack);
        Parts.Tip tip = Iconicwands.parts.tips.get(Integer.parseInt(modelData.substring(1, 3)));
        Parts.Core core = Iconicwands.parts.cores.get(Integer.parseInt(modelData.substring(3, 5)));
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        Parts.Handle handle = Iconicwands.parts.handles.get(Integer.parseInt(modelData.substring(5,7)));
        int rechargeTime = nbtCompound.getInt("recharge_time");
//        int rechargeDelay = nbtCompound.getInt("recharge_delay");
        if (stack.isDamaged()) {
            if (rechargeTime > 0 ) {
                if (world.getTime() % 20 == 0)
                    rechargeTime--;
            }else {
                if (world.getTime() % (core.getRechargeRate()* 20L) == 0) {
//                if (++rechargeDelay % core.getRechargeRate() == 0) {
                    stack.setDamage(stack.getDamage() - (core.getRechargeAmount() + tip.getRechargeAmount()));
//                    rechargeDelay = 0;
                }
            }
        } else if (rechargeTime < core.getRechargeDelay()) {
            rechargeTime = core.getRechargeDelay();
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
            String modelData = getPartCombo(stack);
            Parts.Tip tip = Iconicwands.parts.tips.get(Integer.parseInt(modelData.substring(1,3)));
            Parts.Core core = Iconicwands.parts.cores.get(Integer.parseInt(modelData.substring(3,5)));
            Parts.Handle handle = Iconicwands.parts.handles.get(Integer.parseInt(modelData.substring(5,7)));
            int k;
            int j;
            MagicProjectileEntity persistentProjectileEntity = new MagicProjectileEntity(world, playerEntity);
            persistentProjectileEntity.setNoGravity(true);
            persistentProjectileEntity.setMaxDist(core.getRange());
//            persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, 0, tip.getDivergence());
            persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, tip.getSpeed(), tip.getDivergence());
            if (world.random.nextFloat() <= 0.05f) {//todo wand based?
                persistentProjectileEntity.setCritical(true);
            }

            //todo make this a wand stat
            persistentProjectileEntity.setColor(Color.ofRGB(world.random.nextInt(255),world.random.nextInt(255),world.random.nextInt(255)).getColor());

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
//            rechargeTime = core.getRechargeDelay();
            stack.getOrCreateNbt().putInt("recharge_time", core.getRechargeDelay());
            stack.getOrCreateNbt().putInt("recharge_delay",0);

            if (playerEntity instanceof PlayerEntity player) {
                player.getItemCooldownManager().set(this, core.getFirerate() + handle.getFirerate());
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
        return UseAction.NONE;
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