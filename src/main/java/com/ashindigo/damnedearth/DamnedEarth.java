package com.ashindigo.damnedearth;

import com.ashindigo.damnedearth.blocks.DamnedEarthBlock;
import com.ashindigo.damnedearth.blocks.DamnedEarthMobBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DamnedEarth implements ModInitializer {

    static final String MODID = "damnedearth";
    public static BlockEntityType<?> mobTEType;

    @Override
    public void onInitialize() {
        DamnedEarthBlock damnedEarthBlock = new DamnedEarthBlock(FabricBlockSettings.of(Material.ORGANIC).ticksRandomly().strength(0.7F, 0.7F).sounds(BlockSoundGroup.GRASS).build());
        Registry.register(Registry.BLOCK, new Identifier(MODID, MODID), damnedEarthBlock);
        Registry.register(Registry.ITEM, new Identifier(MODID, MODID), new BlockItem(damnedEarthBlock, new Item.Settings().group(ItemGroup.MISC)));

        DamnedEarthBlock damnedEarthMobBlock = new DamnedEarthMobBlock(FabricBlockSettings.of(Material.ORGANIC).ticksRandomly().strength(0.7F, 0.7F).sounds(BlockSoundGroup.GRASS).build());
        Registry.register(Registry.BLOCK, new Identifier(MODID, MODID + "mob"), damnedEarthMobBlock);
        Registry.register(Registry.ITEM, new Identifier(MODID, MODID + "mob"), new BlockItem(damnedEarthMobBlock, new Item.Settings().group(ItemGroup.MISC)));
        mobTEType = BlockEntityType.Builder.create(DamnedEarthMobBlock.DamnedEarthMobTileEntity::new, damnedEarthMobBlock).build(null);
        Registry.register(Registry.BLOCK_ENTITY, new Identifier(MODID, MODID + "mob"), mobTEType);

    }
}
