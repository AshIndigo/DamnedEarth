package com.ashindigo.damnedearth;

import com.ashindigo.damnedearth.blocks.DamnedEarthBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.text.WordUtils;

public class DamnedEarth implements ModInitializer {

    public static final String MODID = "damnedearth";

    @Override
    public void onInitialize() {
        DamnedEarthBlock damnedEarthBlock = new DamnedEarthBlock(FabricBlockSettings.of(Material.EARTH).build());
        Registry.register(Registry.BLOCK, new Identifier(MODID, MODID), damnedEarthBlock);
        Registry.register(Registry.ITEM, new Identifier(MODID, MODID), new BlockItem(damnedEarthBlock, new Item.Settings().group(ItemGroup.MISC)));
    }
}
