package com.ashindigo.damnedearth;

import com.ashindigo.damnedearth.tileentity.SoulInjectorTileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SoulInjectorRecipe implements Recipe {

    @Override
    public boolean matches(Inventory var1, World var2) {
        return false;
    }

    @Override
    public ItemStack craft(Inventory var1) {
        ItemStack stack = new ItemStack(DamnedEarth.damnedEarthMobBlock);
        CompoundTag tag = new CompoundTag();
        tag.putString("mob", var1.getInvStack(SoulInjectorTileEntity.soulNeedleSlot).getOrCreateTag().getString("mob"));
        stack.setTag(tag);
        return stack;
    }

    @Override
    public boolean fits(int var1, int var2) {
        return false;
    }

    @Override
    public ItemStack getOutput() {
        return ItemStack.EMPTY; // Dont
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ItemStack getRecipeKindIcon() {
        return new ItemStack(DamnedEarth.soulInjectorBlock);
    }

    @Override
    public Identifier getId() {
        return new Identifier(DamnedEarth.MODID, "damnedearthmob");
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }

}
