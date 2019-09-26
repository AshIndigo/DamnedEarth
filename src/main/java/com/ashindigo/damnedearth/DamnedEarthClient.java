package com.ashindigo.damnedearth;

import com.ashindigo.damnedearth.blocks.DamnedEarthMobBlock;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

import static com.ashindigo.damnedearth.DamnedEarth.MODID;

public class DamnedEarthClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ColorProviderRegistry.BLOCK.register(
                (blockState_1, extendedBlockView_1, blockPos_1, int_1) ->
                        extendedBlockView_1 != null && blockPos_1 != null ? Formatting.DARK_PURPLE.getColorValue() : -1,
                Registry.BLOCK.get(new Identifier(MODID, MODID)));

        ColorProviderRegistry.ITEM.register((itemStack_1, int_1) -> Formatting.DARK_PURPLE.getColorValue(), Registry.BLOCK.get(new Identifier(MODID, MODID)));

        ColorProviderRegistry.BLOCK.register(
                (blockState, extendedBlockView, pos, i) -> extendedBlockView != null && pos != null && blockState != null ?
                        extendedBlockView.getBlockEntity(pos) != null ?  SpawnEggItem.forEntity(((DamnedEarthMobBlock.DamnedEarthMobTileEntity) Objects.requireNonNull(extendedBlockView.getBlockEntity(pos))).getMob()) != null ? SpawnEggItem.forEntity(((DamnedEarthMobBlock.DamnedEarthMobTileEntity) Objects.requireNonNull(extendedBlockView.getBlockEntity(pos))).getMob()).getColor(0)  : -1 : -1 : -1,
                Registry.BLOCK.get(new Identifier(MODID, MODID + "mob")));

        ColorProviderRegistry.ITEM.register((itemStack_1, int_1) -> SpawnEggItem.forEntity(Registry.ENTITY_TYPE.get(Identifier.tryParse(itemStack_1.getTag() != null ? itemStack_1.getTag().getString("mob") : null))) != null ? SpawnEggItem.forEntity(Registry.ENTITY_TYPE.get(Identifier.tryParse(itemStack_1.getTag().getString("mob")))).getColor(0) : -1, Registry.BLOCK.get(new Identifier(MODID, MODID + "mob")));
    }
}
