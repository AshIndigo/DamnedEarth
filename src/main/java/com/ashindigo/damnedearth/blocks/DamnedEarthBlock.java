package com.ashindigo.damnedearth.blocks;

import com.ashindigo.damnedearth.MobBlacklist;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class DamnedEarthBlock extends SpreadableBlock {

    private static final int maxBound = 201;
    private static final int minBound = 150;

    public DamnedEarthBlock(Settings block$Settings_1) {
        super(block$Settings_1);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        world.getBlockTickScheduler().schedule(pos, state.getBlock(), Math.max(minBound, world.random.nextInt(maxBound)));
    }

    @Override
    public void onScheduledTick(BlockState blockState_1, World world_1, BlockPos blockPos_1, Random random_1) {
        super.onScheduledTick(blockState_1, world_1, blockPos_1, random_1);
        onRandomTick(blockState_1, world_1, blockPos_1, random_1);
    }

    void spread(World world, BlockPos pos, Random rand) {
        boolean z = rand.nextBoolean();
        BlockPos pos1 = new BlockPos(pos.add(!z ? rand.nextInt(4) - 2 : 0, 0, z ? rand.nextInt(4) - 2 : 0));
        if (Blocks.AIR.getDefaultState().equals(world.getBlockState(pos1.up()))) {
            if (Blocks.GRASS_BLOCK.getDefaultState().equals(world.getBlockState(pos1)) || Blocks.DIRT.getDefaultState().equals(world.getBlockState(pos1))) {
                world.setBlockState(pos1, world.getBlockState(pos));
            }
        }
    }

    void spawnMobs(World world, BlockPos pos, Random rand) {
        if (world.getFluidState(pos.up()).isEmpty()) {
            if (world.getDifficulty() != Difficulty.PEACEFUL) {
                List<Biome.SpawnEntry> spawnList = world.getChunkManager().getChunkGenerator().getEntitySpawnList(EntityCategory.MONSTER, pos);
                int i = rand.nextInt(spawnList.size());
                if (!MobBlacklist.contains(spawnList.get(i).type)) {
                    Entity entity = spawnList.get(i).type.create(world);
                    if (world.getEntities(HostileEntity.class, new Box(pos, pos.add(1, 3, 1))).size() > 1) {
                        return;
                    }
                    if (entity instanceof HostileEntity) {
                        Objects.requireNonNull(entity).setPosition(pos.getX() + .5, pos.getY() + 1, pos.getZ() + .5);
                        world.spawnEntity(entity);
                    }
                }
            }
        }
    }


    @Override
    @SuppressWarnings("deprecation")
    public void onRandomTick(BlockState state, World world, BlockPos pos, Random rand) {
        world.getBlockTickScheduler().schedule(pos, state.getBlock(), Math.max(minBound, world.random.nextInt(maxBound)));
        if (!world.isClient) {
            if (world.isSkyVisible(pos.up()) && world.getLightLevel(pos.up()) >= 12) { // Kill off dirt if bright enough and in sunlight
                world.setBlockState(pos, Blocks.DIRT.getDefaultState());
            } else {
                if (world.getLightLevel(pos.up()) < 10) {
                    spread(world, pos, rand);
                    spawnMobs(world, pos, rand);
                }
            }
        }
    }
}
