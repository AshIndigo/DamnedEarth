package com.ashindigo.damnedearth.blocks;

import com.ashindigo.damnedearth.DamnedEarth;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
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
    public void spread(BlockState state, World world, BlockPos pos, Random rand) {
        boolean z = rand.nextBoolean();
        BlockPos pos1 = new BlockPos(pos.add(!z ? rand.nextInt(4) - 2 : 0, 0, z ? rand.nextInt(4) - 2 : 0));
        if (Blocks.AIR.getDefaultState().equals(world.getBlockState(pos1.up()))) {
            if (Blocks.GRASS_BLOCK.getDefaultState().equals(world.getBlockState(pos1)) || Blocks.DIRT.getDefaultState().equals(world.getBlockState(pos1))) {
                world.setBlockState(pos1, world.getBlockState(pos));
                EntityType<?> type = Registry.ENTITY_TYPE.get(Identifier.tryParse(world.getBlockEntity(pos).toTag(new CompoundTag()).getString("mob")));
                ((DamnedEarthMobTileEntity) world.getBlockEntity(pos1)).setMob(type);
                world.getBlockEntity(pos1).markDirty();
            }
        }
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
    void spawnMobs(BlockState state, World world, BlockPos pos, Random rand) {
        if (world.getDifficulty() != Difficulty.PEACEFUL) {
            Entity toSpawn = ((DamnedEarthMobTileEntity) Objects.requireNonNull(world.getBlockEntity(pos))).getMob().create(world);
            Objects.requireNonNull(toSpawn).setPosition(pos.getX(), pos.getY() + 1, pos.getZ());
            world.spawnEntity(toSpawn);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ItemStack getPickStack(BlockView blockView_1, BlockPos blockPos_1, BlockState blockState_1) {
        ItemStack stack = new ItemStack(this);
        CompoundTag tag = new CompoundTag();
        tag.putString("mob", blockView_1.getBlockEntity(blockPos_1).toTag(new CompoundTag()).getString("mob"));
        stack.setTag(tag);
        return stack;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView var1) {
        return new DamnedEarthMobTileEntity();
    }

    public static class DamnedEarthMobTileEntity extends BlockEntity {

        EntityType<?> mob = EntityType.PIG;

        public DamnedEarthMobTileEntity() {
            super(DamnedEarth.mobTEType);
        }

        @Override
        public BlockEntityUpdateS2CPacket toUpdatePacket() {
            return new BlockEntityUpdateS2CPacket(this.pos, 2, this.toInitialChunkDataTag());
        }

        @Override
        public CompoundTag toInitialChunkDataTag() {
            return this.toTag(new CompoundTag());
        }

        @Override
        public void fromTag(CompoundTag tag) {
            super.fromTag(tag);
            mob = Registry.ENTITY_TYPE.get(Identifier.tryParse(tag.getString("mob")));
        }

        @Override
        public CompoundTag toTag(CompoundTag tag) {
            super.toTag(tag);
            tag.putString("mob", EntityType.getId(mob).toString());
            return tag;
        }

        public EntityType<?> getMob() {
            return mob;
        }

        void setMob(EntityType<?> mob) {
            this.mob = mob;
        }
    }
}
