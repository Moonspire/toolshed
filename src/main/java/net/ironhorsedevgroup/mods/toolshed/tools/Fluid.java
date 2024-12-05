package net.ironhorsedevgroup.mods.toolshed.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
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

    public static void addFluid(Level world, BlockPos pos, int tank, FluidStack fluid) {
        BlockEntity _ent = world.getBlockEntity(pos);
        if (_ent != null)
            _ent.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                    .ifPresent(capability -> capability.fill(fluid, IFluidHandler.FluidAction.EXECUTE));
    }

    public static void addFluid(Level world, BlockPos pos, int tank, int amount, ResourceLocation fluid) {
        net.minecraft.world.level.material.Fluid fluids = ForgeRegistries.FLUIDS.getValue(fluid);
        if (fluids != null) {
            addFluid(world, pos, tank, new FluidStack(fluids, amount));
        }
    }

    public static void addFluid(Level world, BlockPos pos, int tank, int amount) {
        addFluid(world, pos, tank, new FluidStack(Fluids.WATER, amount));
    }

    public static void drainFluid(Level world, BlockPos pos, int tank, int amount) {
        BlockEntity _ent = world.getBlockEntity(pos);
        if (_ent != null)
            _ent.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                    .ifPresent(capability -> capability.drain(new FluidStack(getFluid(world, pos, tank).getFluid(), amount), IFluidHandler.FluidAction.EXECUTE));
    }

    private static boolean drawFluid(Level world, InteractionHand hand, Player player, BlockPos pos, int tank, int drawAmount) { //Draws fluid into container from block
        if (player.isCrouching()) {
            return false;
        }
        boolean retval = false;
        FluidStack fluid = getFluid(world, pos, tank);
        if (getFluidTankLevel(world, pos, tank) >= drawAmount) {
            ItemStack itemstack = player.getItemInHand(hand);
            ItemStack newstack = itemstack;
            Item item = itemstack.getItem();
            if (item instanceof BucketItem) {
                world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BUCKET_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                world.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
                newstack = ItemUtils.createFilledResult(itemstack, player, new ItemStack(fluid.getRawFluid().getBucket()));
                retval = true;
            } else if (item instanceof BottleItem && fluid.getFluid() == Fluids.WATER) {
                world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                world.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
                newstack = ItemUtils.createFilledResult(itemstack, player, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER));
                retval = true;
            }
            if (retval) {
                drainFluid(world, pos, tank, drawAmount);
            }
            player.setItemInHand(hand, newstack);
        } else {
            player.displayClientMessage(Component.literal("Not enough fluid in source"), (true));
        }
        return retval;
    }

    public static boolean drawFluid(Level world, InteractionHand hand, Player player, BlockPos pos, int tank) {
        if (player.isCrouching()) {
            return false;
        }
        boolean retval = false;
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();
        if (item instanceof BucketItem) {
            retval = drawFluid(world, hand, player, pos, tank, 1000);
        } else if (item instanceof BottleItem) {
            retval = drawFluid(world, hand, player, pos, tank, 333);
        }
        return retval;
    }
}
