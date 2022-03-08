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
import website.skylorbeck.minecraft.iconicwands.Iconicwands;
import website.skylorbeck.minecraft.iconicwands.config.Parts;
import website.skylorbeck.minecraft.iconicwands.entity.MagicProjectileEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;


public class IconicWand extends RangedWeaponItem{
    private Parts.Tip tip = Iconicwands.parts.tips.get(0);
    private Parts.Core core = Iconicwands.parts.cores.get(0);
    private Parts.Handle handle = Iconicwands.parts.handles.get(0);

    int rechargeTime = 0;//counts down til recharge can start, resets on fire
    int rechargeDelay = 0;//counts up til next recharge tick

    public IconicWand(Settings settings) {
        super(settings);
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        saveComponents(stack);
        super.onCraft(stack, world, player);
    }

    private void saveComponents(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt("custom_model_data", Integer.parseInt(1 + String.format("%02d", Iconicwands.parts.tips.indexOf(tip)) + String.format("%02d", Iconicwands.parts.cores.indexOf(core)) + String.format("%02d", Iconicwands.parts.handles.indexOf(handle))));
        Logger.getGlobal().log(Level.SEVERE, nbt.getInt("custom_model_data") + "");
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (context.isAdvanced() || Screen.hasShiftDown()) {
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
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        IconicWand wand =  ((IconicWand)stack.getItem());
        DefaultedList<ItemStack> defaultedList = DefaultedList.of();
        defaultedList.add(new ItemStack(Registry.ITEM.get(new Identifier(wand.tip.getIdentifier()))));
        defaultedList.add(new ItemStack(Registry.ITEM.get(new Identifier(wand.core.getIdentifier()))));
        defaultedList.add(new ItemStack(Registry.ITEM.get(new Identifier(wand.handle.getIdentifier()))));
//        defaultedList.add(ItemStack.EMPTY);
        return Optional.of(new WandTooltipData(defaultedList));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient) return;
        if (stack.isDamaged()) {
            if (rechargeTime > 0)
                rechargeTime--;
            else {
                if (++rechargeDelay % core.getRechargeRate() == 0) {
                    stack.setDamage(stack.getDamage() - (core.getRechargeAmount()+tip.getRechargeAmount()));
                    rechargeDelay = 0;
                }
            }
        } else if (rechargeTime < core.getRechargeDelay()) {
            resetRechargeTime();
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public boolean isUsedOnRelease(ItemStack stack) {
        return true;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity playerEntity, int remainingUseTicks) {
        if (!world.isClient) {
            int k;
            int j;
            MagicProjectileEntity persistentProjectileEntity = new MagicProjectileEntity(world, playerEntity);
            persistentProjectileEntity.setNoGravity(true);
            persistentProjectileEntity.setMaxDist(core.getRange());
            persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, tip.getSpeed(), tip.getDivergence());
            if (world.random.nextFloat() <= 0.05f) {
                persistentProjectileEntity.setCritical(true);
            }
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
            resetRechargeTime();
            if (playerEntity instanceof PlayerEntity player) {
                player.getItemCooldownManager().set(this, core.getFirerate() + handle.getFirerate());
//                this.handle = Iconicwands.parts.handles.get(world.random.nextInt(Iconicwands.parts.handles.size()));
//                this.core = Iconicwands.parts.cores.get(world.random.nextInt(Iconicwands.parts.cores.size()));
//                this.tip = Iconicwands.parts.tips.get(world.random.nextInt(Iconicwands.parts.tips.size()));
            }
            saveComponents(stack);
        }
        super.onStoppedUsing(stack, world, playerEntity, remainingUseTicks);
    }

    private void resetRechargeTime() {
        rechargeTime = core.getRechargeDelay();
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

    public Parts.Tip getTip() {
        return tip;
    }

    public void setTip(Parts.Tip tip) {
        this.tip = tip;
    }

    public Parts.Core getCore() {
        return core;
    }

    public void setCore(Parts.Core core) {
        this.core = core;
    }

    public Parts.Handle getHandle() {
        return handle;
    }

    public void setHandle(Parts.Handle handle) {
        this.handle = handle;
    }

    public void setComponents(Parts.Core core, Parts.Handle handle, Parts.Tip tip) {
        this.core = core;
        this.handle = handle;
        this.tip = tip;
    }
}