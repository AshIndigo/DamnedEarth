package com.ashindigo.damnedearth;

import net.minecraft.entity.EntityType;

import java.util.ArrayList;

public class MobBlacklist {

    private static final ArrayList<EntityType<?>> blacklist = new ArrayList<>();

    public static void init() {
        addEntry(EntityType.AREA_EFFECT_CLOUD);
        addEntry(EntityType.ARMOR_STAND);
        addEntry(EntityType.ARROW);
        addEntry(EntityType.BOAT);
        addEntry(EntityType.BOAT);
        addEntry(EntityType.COMMAND_BLOCK_MINECART);
        addEntry(EntityType.DRAGON_FIREBALL);
        addEntry(EntityType.EGG);
        addEntry(EntityType.END_CRYSTAL);
        addEntry(EntityType.ENDER_DRAGON);
        addEntry(EntityType.ENDER_PEARL);
        addEntry(EntityType.EVOKER_FANGS);
        addEntry(EntityType.EXPERIENCE_BOTTLE);
        addEntry(EntityType.EXPERIENCE_ORB);
        addEntry(EntityType.EYE_OF_ENDER);
        addEntry(EntityType.FALLING_BLOCK);
        addEntry(EntityType.FIREBALL);
        addEntry(EntityType.FIREWORK_ROCKET);
        addEntry(EntityType.FISHING_BOBBER);
        addEntry(EntityType.FURNACE_MINECART);
        addEntry(EntityType.HOPPER_MINECART);
        addEntry(EntityType.ITEM);
        addEntry(EntityType.ITEM_FRAME);
        addEntry(EntityType.LEASH_KNOT);
        addEntry(EntityType.LIGHTNING_BOLT);
        addEntry(EntityType.LLAMA_SPIT);
        addEntry(EntityType.MINECART);
        addEntry(EntityType.PAINTING);
        addEntry(EntityType.PLAYER);
        addEntry(EntityType.POTION);
        addEntry(EntityType.SHULKER_BULLET);
        addEntry(EntityType.SMALL_FIREBALL);
        addEntry(EntityType.SNOWBALL);
        addEntry(EntityType.SPAWNER_MINECART);
        addEntry(EntityType.SMALL_FIREBALL);
        addEntry(EntityType.SPECTRAL_ARROW);
        addEntry(EntityType.TNT);
        addEntry(EntityType.TNT_MINECART);
        addEntry(EntityType.TRADER_LLAMA);
        addEntry(EntityType.TRIDENT);
        addEntry(EntityType.WANDERING_TRADER);
        addEntry(EntityType.WITHER);
}

    @SuppressWarnings("WeakerAccess")
    public static void addEntry(EntityType<?> type) {
        blacklist.add(type);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean contains(EntityType<?> type) {
        return blacklist.contains(type);
    }
}
