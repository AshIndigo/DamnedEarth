package com.ashindigo.damnedearth;

import com.ashindigo.damnedearth.gui.SoulInjectorGui;
import com.ashindigo.damnedearth.tileentity.DamnedEarthMobTileEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ExtendedBlockView;

import java.util.Objects;

@SuppressWarnings("unused")
public class DamnedEarthClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(DamnedEarth.soulInjectorID, (syncId, identifier, player, buf) -> new SoulInjectorGui<>(DamnedEarth.soulInjectorContainer.create(syncId, identifier, player, buf), player.inventory));
        ColorProviderRegistry.BLOCK.register((blockState, extendedBlockView, blockPos, i) -> extendedBlockView != null && blockPos != null ? Objects.requireNonNull(Formatting.DARK_PURPLE.getColorValue()) : -1, Registry.BLOCK.get(DamnedEarth.damnedEarthID));
        ColorProviderRegistry.ITEM.register((itemStack_1, int_1) -> Objects.requireNonNull(Formatting.DARK_PURPLE.getColorValue()), Registry.BLOCK.get(DamnedEarth.damnedEarthID));
        ColorProviderRegistry.BLOCK.register((blockState, extendedBlockView, pos, i) -> getColor(blockState, extendedBlockView, pos), Registry.BLOCK.get(DamnedEarth.damnedEarthMobID));
        ColorProviderRegistry.ITEM.register((stack, int_1) -> getColor(stack), Registry.BLOCK.get(DamnedEarth.damnedEarthMobID));
    }

    private int getColor(BlockState state, ExtendedBlockView extendedBlockView, BlockPos pos) {
        if (state != null && pos != null && extendedBlockView != null) {
            if (extendedBlockView.getBlockEntity(pos) instanceof DamnedEarthMobTileEntity) {
                if (SpawnEggItem.forEntity(((DamnedEarthMobTileEntity) Objects.requireNonNull(extendedBlockView.getBlockEntity(pos))).getMob()) != null) {
                    return SpawnEggItem.forEntity(((DamnedEarthMobTileEntity) Objects.requireNonNull(extendedBlockView.getBlockEntity(pos))).getMob()).getColor(0);
                }
            }
        }
        return -1;
    }

    private int getColor(ItemStack stack) {
        if (stack.getTag() != null) {
            if (SpawnEggItem.forEntity(Registry.ENTITY_TYPE.get(Identifier.tryParse(stack.getTag().getString("mob")))) != null) {
                return SpawnEggItem.forEntity(Registry.ENTITY_TYPE.get(Identifier.tryParse(stack.getTag().getString("mob")))).getColor(0);
            }
        }
        return -1;
    }
}
