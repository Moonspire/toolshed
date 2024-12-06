package net.ironhorsedevgroup.mods.toolshed.abstracts;

import net.ironhorsedevgroup.mods.toolshed.tools.Fluid;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class AbstractFluidCollector extends Block {
    public static int rate;
    public static ResourceLocation fluid;

    public AbstractFluidCollector(Integer collection_rate, Properties blockproperties) {
        super(blockproperties);
        rate = collection_rate;
        fluid = ForgeRegistries.FLUIDS.getKey(Fluids.WATER);
    }

    public AbstractFluidCollector(Integer collection_rate, ResourceLocation generated_fluid, Properties blockproperties) {
        super(blockproperties);
        rate = collection_rate;
        fluid = generated_fluid;
    }

    public void tick(BlockState blockstate, ServerLevel world, BlockPos pos, RandomSource random) {
        super.tick(blockstate, world, pos, random);
        addFluidToBlock(world, pos);
        world.scheduleTick(pos, this, 60);
    }

    public void addFluidToBlock(Level world, BlockPos pos) {
    }

    public ResourceLocation getFluid() {
        return fluid;
    }

    @Override
    public InteractionResult use(BlockState blockstate, Level world, BlockPos pos, Player entity, InteractionHand hand, BlockHitResult hit) {
        super.use(blockstate, world, pos, entity, hand, hit);

        if (Fluid.drawFluid(world, hand, entity, pos, 1)) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
