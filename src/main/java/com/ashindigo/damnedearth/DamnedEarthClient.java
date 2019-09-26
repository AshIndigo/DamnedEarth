package com.ashindigo.damnedearth;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;
import net.minecraft.block.ReplaceableTallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DamnedEarthClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ColorProviderRegistry.BLOCK.register(
                (blockState_1, extendedBlockView_1, blockPos_1, int_1) ->
                        extendedBlockView_1 != null && blockPos_1 != null ? Formatting.DARK_PURPLE.getColorValue() : -1,
                Registry.BLOCK.get(new Identifier(DamnedEarth.MODID, DamnedEarth.MODID)));
    }
}
