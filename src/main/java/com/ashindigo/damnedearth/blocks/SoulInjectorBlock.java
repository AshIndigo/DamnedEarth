package com.ashindigo.damnedearth.blocks;

import com.ashindigo.damnedearth.DamnedEarth;
import com.ashindigo.damnedearth.tileentity.SoulInjectorTileEntity;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class SoulInjectorBlock extends BlockWithEntity {

    private static final DirectionProperty FACING;
    public static final BooleanProperty LIT;

    public SoulInjectorBlock(Settings block$Settings_1) {
        super(block$Settings_1);
        this.setDefaultState(this.stateFactory.getDefaultState().with(FACING, Direction.NORTH).with(LIT, false));
    }

    @Override
    public int getLuminance(BlockState blockState_1) {
        return blockState_1.get(LIT) ? super.getLuminance(blockState_1) : 0;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext itemPlacementContext_1) {
        return this.getDefaultState().with(FACING, itemPlacementContext_1.getPlayerFacing().getOpposite());
    }

    @Override
    public void onBlockRemoved(BlockState blockState_1, World world_1, BlockPos blockPos_1, BlockState blockState_2, boolean boolean_1) {
        if (blockState_1.getBlock() != blockState_2.getBlock()) {
            BlockEntity blockEntity_1 = world_1.getBlockEntity(blockPos_1);
            if (blockEntity_1 instanceof SoulInjectorTileEntity) {
                ItemScatterer.spawn(world_1, blockPos_1, ((SoulInjectorTileEntity) blockEntity_1).getInventory(null, null, null));
                world_1.updateHorizontalAdjacent(blockPos_1, this);
            }

            super.onBlockRemoved(blockState_1, world_1, blockPos_1, blockState_2, boolean_1);
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState blockState_1) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView var1) {
        return new SoulInjectorTileEntity();
    }

    @Override
    public boolean activate(BlockState blockState_1, World world_1, BlockPos pos, PlayerEntity player, Hand hand_1, BlockHitResult blockHitResult_1) {
        if(!world_1.isClient) {
            ContainerProviderRegistry.INSTANCE.openContainer(new Identifier(DamnedEarth.MODID, "soulinjector"), player, (buf) -> buf.writeBlockPos(pos));
        }

        return true;
    }

    @Override
    public BlockState rotate(BlockState blockState_1, BlockRotation blockRotation_1) {
        return blockState_1.with(FACING, blockRotation_1.rotate(blockState_1.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState_1, BlockMirror blockMirror_1) {
        return blockState_1.rotate(blockMirror_1.getRotation(blockState_1.get(FACING)));
    }

    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory$Builder_1) {
        stateFactory$Builder_1.add(FACING, LIT);
    }

    static {
        FACING = HorizontalFacingBlock.FACING;
        LIT = RedstoneTorchBlock.LIT;
    }
}
