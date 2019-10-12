package com.ashindigo.damnedearth;

import com.ashindigo.damnedearth.blocks.DamnedEarthBlock;
import com.ashindigo.damnedearth.blocks.DamnedEarthMobBlock;
import com.ashindigo.damnedearth.blocks.SoulInjectorBlock;
import com.ashindigo.damnedearth.tileentity.DamnedEarthMobTileEntity;
import com.ashindigo.damnedearth.tileentity.SoulInjectorTileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.container.ContainerFactory;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.container.Container;
import net.minecraft.container.ContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.loot.ConstantLootTableRange;
import net.minecraft.world.loot.entry.ItemEntry;

import java.util.List;
import java.util.Objects;

// TODO Texture for soul injector, speck of evil
public class DamnedEarth implements ModInitializer {

    public static final String MODID = "damnedearth";
    static final Identifier damnedEarthID = new Identifier(MODID, MODID);
    static final Identifier damnedEarthMobID = new Identifier(MODID, MODID + "mob");
    static final Identifier soulInjectorID = new Identifier(MODID, "soulinjector");
    public static BlockEntityType<?> mobTEType;
    public static BlockEntityType<?> soulInjectorType;
    static ContainerFactory<Container> soulInjectorContainer;
    static Block soulInjectorBlock;
    static DamnedEarthBlock damnedEarthMobBlock;

    @Override
    public void onInitialize() {
        // Damned Earth
        DamnedEarthBlock damnedEarthBlock = new DamnedEarthBlock(FabricBlockSettings.of(Material.ORGANIC).ticksRandomly().strength(0.7F, 0.7F).sounds(BlockSoundGroup.GRASS).build());
        Registry.register(Registry.BLOCK, damnedEarthID, damnedEarthBlock);
        Registry.register(Registry.ITEM, damnedEarthID, new BlockItem(damnedEarthBlock, new Item.Settings().group(ItemGroup.MISC)));
        // Monstrous Damned Earth
        damnedEarthMobBlock = new DamnedEarthMobBlock(FabricBlockSettings.of(Material.ORGANIC).ticksRandomly().strength(0.7F, 0.7F).sounds(BlockSoundGroup.GRASS).build());
        Registry.register(Registry.BLOCK, damnedEarthMobID, damnedEarthMobBlock);
        Registry.register(Registry.ITEM, damnedEarthMobID, new BlockItem(damnedEarthMobBlock, new Item.Settings().group(ItemGroup.MISC)) {
            @Override
            @Environment(EnvType.CLIENT)
            public void appendTooltip(ItemStack stack, World world, List<Text> list, TooltipContext context) {
                if (stack.getTag() != null) {
                    list.add(new TranslatableText(Registry.ENTITY_TYPE.get(Identifier.tryParse(stack.getTag().getString("mob"))).getTranslationKey()));
                }
            }
        });
        mobTEType = BlockEntityType.Builder.create(DamnedEarthMobTileEntity::new, damnedEarthMobBlock).build(null);
        Registry.register(Registry.BLOCK_ENTITY, damnedEarthMobID, mobTEType);
        soulInjectorBlock = new SoulInjectorBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.GRAY).lightLevel(13).build());
        // Soul Injector
        Registry.register(Registry.BLOCK, soulInjectorID, soulInjectorBlock);
        Registry.register(Registry.ITEM, soulInjectorID, new BlockItem(soulInjectorBlock, new Item.Settings().group(ItemGroup.MISC).maxCount(1)));
        soulInjectorType = BlockEntityType.Builder.create(SoulInjectorTileEntity::new, soulInjectorBlock).build(null);
        Registry.register(Registry.BLOCK_ENTITY, soulInjectorID, soulInjectorType);
        soulInjectorContainer = (syncId, identifier, player, buf) -> ((ContainerProvider) Objects.requireNonNull(player.world.getBlockEntity(buf.readBlockPos()))).createMenu(syncId, player.inventory, player);
        ContainerProviderRegistry.INSTANCE.registerFactory(soulInjectorID, soulInjectorContainer);
        // Soul Needle
        ItemSoulNeedle soulNeedle = new ItemSoulNeedle(new Item.Settings().group(ItemGroup.MISC));
        Registry.register(Registry.ITEM, new Identifier(MODID, "soulneedle"), soulNeedle);
        // Speck of evil
        Registry.register(Registry.ITEM, new Identifier(MODID, "speckofevil"), new Item(new Item.Settings().group(ItemGroup.MISC)));
        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
            if (new Identifier("minecraft", "entities/wither_skeleton").equals(id)) {
                FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                        .withRolls(ConstantLootTableRange.create(1))
                        .withEntry(ItemEntry.builder(Registry.ITEM.get(new Identifier(MODID, "speckofevil"))));

                supplier.withPool(poolBuilder);
            }
        });
        MobBlacklist.init();
    }
}
