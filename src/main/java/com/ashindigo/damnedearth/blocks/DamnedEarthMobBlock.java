package com.ashindigo.damnedearth.blocks;

import com.ashindigo.damnedearth.MobBlacklist;
import com.ashindigo.damnedearth.tileentity.DamnedEarthMobTileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Random;

public class DamnedEarthMobBlock extends DamnedEarthBlock implements BlockEntityProvider {

    public DamnedEarthMobBlock(Settings block$Settings_1) {
        super(block$Settings_1);
    }

    @Override
    public void spread(World world, BlockPos pos, Random rand) {
        // NO-OP
    }

    @Override
    public void onPlaced(World world_1, BlockPos blockPos_1, BlockState blockState_1, LivingEntity livingEntity_1, ItemStack itemStack_1) {
        BlockEntity blockEntity_1 = world_1.getBlockEntity(blockPos_1);
        if (blockEntity_1 instanceof DamnedEarthMobTileEntity) {
            if (itemStack_1.getTag() != null) {
                ((DamnedEarthMobTileEntity) blockEntity_1).setMob(Registry.ENTITY_TYPE.get(Identifier.tryParse(itemStack_1.getTag().getString("mob"))));
                blockEntity_1.markDirty();
            }
        }
    }

    @Override
    void spawnMobs(World world, BlockPos pos, Random rand) {
        if (world.getDifficulty() != Difficulty.PEACEFUL) {
            if (!MobBlacklist.contains(((DamnedEarthMobTileEntity) Objects.requireNonNull(world.getBlockEntity(pos))).getMob())) {
                if (world.getEntities(HostileEntity.class, new Box(pos, pos.add(1, 3, 1))).size() > 1) {
                    return;
                }
                Entity toSpawn = ((DamnedEarthMobTileEntity) Objects.requireNonNull(world.getBlockEntity(pos))).getMob().create(world);
                Objects.requireNonNull(toSpawn).setPosition(pos.getX() + .5, pos.getY() + 1, pos.getZ() + .5);
                if (world.isAreaNotEmpty(toSpawn.getBoundingBox()) || !world.doesNotCollide(toSpawn)) {
                    return;
                }
                world.spawnEntity(toSpawn);
            }
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ItemStack getPickStack(BlockView blockView_1, BlockPos blockPos_1, BlockState blockState_1) {
        ItemStack stack = new ItemStack(this);
        CompoundTag tag = new CompoundTag();
        tag.putString("mob", Objects.requireNonNull(blockView_1.getBlockEntity(blockPos_1)).toTag(new CompoundTag()).getString("mob"));
        stack.setTag(tag);
        return stack;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView var1) {
        return new DamnedEarthMobTileEntity();
    }

}
