package net.ironhorsedevgroup.mods.toolshed.tools;

import net.ironhorsedevgroup.mods.toolshed.Toolshed;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Fluid {
    public static boolean isBlockWaterlogged(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED);
    }

    public static boolean isBlockWaterlogged(LevelAccessor level, BlockPos pos) {
        return isBlockWaterlogged(level.getBlockState(pos));
    }

    public static int getFluidTankCapacity(LevelAccessor level, BlockPos pos, int tank) {
        AtomicInteger _retval = new AtomicInteger(0);
        BlockEntity _ent = level.getBlockEntity(pos);
        if (_ent != null)
            _ent.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                    .ifPresent(capability -> _retval.set(capability.getTankCapacity(tank)));
        return _retval.get();
    }

    public static int getFluidTankLevel(LevelAccessor level, BlockPos pos, int tank) {
        AtomicInteger _retval = new AtomicInteger(0);
        BlockEntity _ent = level.getBlockEntity(pos);
        if (_ent != null)
            _ent.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                    .ifPresent(capability -> _retval.set(capability.getFluidInTank(tank).getAmount()));
        return _retval.get();
    }

    public static int getFluidToFull(LevelAccessor level, BlockPos pos, int tank) {
        return getFluidTankCapacity(level, pos, tank) - getFluidTankLevel(level, pos, tank);
    }

    public static FluidStack getFluid(LevelAccessor level, BlockPos pos, int tank) {
        AtomicReference<FluidStack> retStack = new AtomicReference<>();
        BlockEntity _ent = level.getBlockEntity(pos);
        if (_ent != null)
            _ent.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                    .ifPresent(capability -> retStack.set(capability.getFluidInTank(tank)));
        return retStack.get();
    }

    public static void addFluid(Level level, BlockPos pos, int tank, FluidStack fluid) {
        if (level.isClientSide) {
            return;
        }
        FluidStack existingFluid = getFluid(level, pos, tank);
        if (fluid.getRawFluid().isSame(existingFluid.getRawFluid()) && !fluid.getOrCreateTag().equals(existingFluid.getOrCreateTag())) {
            Toolshed.LOGGER.info("Converting fluid found at {} to match NBT data.", pos);
            int amount = existingFluid.getAmount();
            drainFluid(level, pos, tank, amount);
            fluid.setAmount(fluid.getAmount() + amount);
        }
        BlockEntity _ent = level.getBlockEntity(pos);
        if (_ent != null)
            _ent.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                    .ifPresent(capability -> capability.fill(fluid, IFluidHandler.FluidAction.EXECUTE));
    }

    public static void addFluid(Level level, BlockPos pos, int tank, int amount, ResourceLocation fluid) {
        net.minecraft.world.level.material.Fluid fluids = ForgeRegistries.FLUIDS.getValue(fluid);
        if (fluids != null) {
            addFluid(level, pos, tank, new FluidStack(fluids, amount));
        }
    }

    public static void addFluid(Level level, BlockPos pos, int tank, int amount) {
        addFluid(level, pos, tank, new FluidStack(Fluids.WATER, amount));
    }

    public static boolean drainFluid(Level level, BlockPos pos, int tank, int amount) {
        if (getFluidTankLevel(level, pos, tank) >= amount) {
            if (level.isClientSide) {
                return true;
            }
            FluidStack fluid = getFluid(level, pos, tank);
            fluid.setAmount(fluid.getAmount() - amount);
            return true;
        }
        return false;
    }

    public static boolean drawBucket(Level level, InteractionHand hand, Player player, BlockPos pos, int tank) {
        boolean retval = false;
        FluidStack fluid = getFluid(level, pos, tank);
        Item bucket = fluid.getFluid().getBucket();
        if (bucket instanceof Item) {
            if (getFluidTankLevel(level, pos, tank) >= 1000) {
                ItemStack transform = bucket.getDefaultInstance();
                transform.deserializeNBT(fluid.getTag());
                ItemUtils.createFilledResult(player.getItemInHand(hand), player, transform);
                if (!player.isCreative()) {
                    drainFluid(level, pos, tank, 1000);
                }
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BUCKET_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
            } else {
                player.displayClientMessage(Component.literal("Not enough fluid in source!"), false);
                return false;
            }
        } else {
            player.displayClientMessage(Component.literal("Not a bucketable fluid!"), false);
            return false;
        }
        return retval;
    }

    /*
    public static boolean drawBottle(Level level, InteractionHand hand, Player player, BlockPos pos, int tank) {
        Toolshed.LOGGER.info("Drawing Bottle");
        boolean retval = false;
        FluidStack fluid = getFluid(level, pos, tank);
        if (fluid.getFluid().isSame(Fluids.WATER)) {
            Item bottle = Items.POTION;
            if (getFluidTankLevel(level, pos, tank) >= 333) {
                ItemStack transform = PotionUtils.setPotion(bottle.getDefaultInstance(), Potions.WATER);
                transform.deserializeNBT(fluid.getTag());
                ItemUtils.createFilledResult(player.getItemInHand(hand), player, transform);
                if (!player.isCreative()) {
                    drainFluid(level, pos, tank, 333);
                }
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BUCKET_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
            } else {
                player.displayClientMessage(Component.literal("Not enough fluid in source!"), false);
                return false;
            }
        } else {
            player.displayClientMessage(Component.literal("Not a bottleable fluid!"), false);
            return false;
        }
        return retval;
    }
     */

    public static boolean drawBottle(Level level, InteractionHand hand, Player player, BlockPos pos, int tank) {
        FluidStack fluid = getFluid(level, pos, tank);
        if (fluid.getFluid().isSame(Fluids.WATER)) {
            CompoundTag tags = fluid.getTag();
            if ((player.isCreative()) || drainFluid(level, pos, tank, 333)) {
                Item bottle = Items.POTION;
                ItemStack transform = new ItemStack(bottle);
                CompoundTag newTags = transform.getOrCreateTag();
                for (String key : tags.getAllKeys()) {
                    newTags.put(key, Objects.requireNonNull(tags.get(key)));
                }
                NBT.putLocationTag(transform, "Potion", new ResourceLocation("minecraft:water"));
                ItemStack emptyStack = player.getItemInHand(hand);
                if (player.isCreative() || emptyStack.getCount() > 1) {
                    ItemUtils.createFilledResult(emptyStack, player, transform);
                } else {
                    player.setItemInHand(hand, transform);
                }
                return true;
            }
        }
        if (!level.isClientSide()){
            player.displayClientMessage(Component.literal("Could not bottle fluid!"), false);
        }
        return false;
    }

    public static boolean drawFluid(Level level, InteractionHand hand, Player player, BlockPos pos, int tank) {
        if (player.isCrouching()) {
            return false;
        }
        boolean retval = false;
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(Items.BUCKET)) {
            retval = drawBucket(level, hand, player, pos, tank);
        } else if (itemstack.is(Items.GLASS_BOTTLE)) {
            retval = drawBottle(level, hand, player, pos, tank);
        }
        return retval;
    }
}
