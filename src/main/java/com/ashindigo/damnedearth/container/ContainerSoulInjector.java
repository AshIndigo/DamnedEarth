package com.ashindigo.damnedearth.container;

import com.ashindigo.damnedearth.ItemSoulNeedle;
import com.ashindigo.damnedearth.blocks.DamnedEarthBlock;
import com.ashindigo.damnedearth.tileentity.SoulInjectorTileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.container.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public class ContainerSoulInjector extends Container {

    private final SoulInjectorTileEntity te;
    private final PropertyDelegate propertyDelegate;

    public ContainerSoulInjector(int syncId, PlayerInventory inventory, SoulInjectorTileEntity blockEntity, PropertyDelegate delegate) {
        super(null, syncId);
        propertyDelegate = delegate;
        te = blockEntity;

        // Fuel Slot
        this.addSlot(new Slot(te.getInventory(null, null, null), 0, 56, 53) { // Fuel Slot
            @Override
            public boolean canInsert(ItemStack itemStack_1) {
                return AbstractFurnaceBlockEntity.createFuelTimeMap().containsKey(itemStack_1.getItem());
            }
        });
        this.addSlot(new Slot(te.getInventory(null, null, null), 1, 26, 17) { // Soul Needle
            @Override
            public boolean canInsert(ItemStack itemStack_1) {
                return itemStack_1.getItem() instanceof ItemSoulNeedle;
            }
        });
        this.addSlot(new Slot(te.getInventory(null, null, null), 2, 56, 17) { // Damned Earth Slot
            @Override
            public boolean canInsert(ItemStack stack) {
                return Block.getBlockFromItem(stack.getItem()) instanceof DamnedEarthBlock;
            }
        });
        
        this.addSlot(new Slot(te.getInventory(null, null, null), 3, 116, 35) { // Damned Mob Earth Result Slot
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });
        // Player Inventory
        for(int i = 0; i < 3; ++i) {
            for(int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(inventory, x + i * 9 + 9, 8 + x * 18, 84 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    // TODO Proper Slot transfer behavior
    public ItemStack transferSlot(PlayerEntity player, int i) {
        ItemStack stack1 = ItemStack.EMPTY;
        Slot slot1 = this.slotList.get(i);
        if (slot1 != null && slot1.hasStack()) {
            ItemStack stack2 = slot1.getStack();
            stack1 = stack2.copy();
            if (i == 2) {
                if (!this.insertItem(stack2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot1.onStackChanged(stack2, stack1);
            } else if (i != 1 && i != 0) {
                if (!this.insertItem(stack2, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(stack2, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (stack2.isEmpty()) {
                slot1.setStack(ItemStack.EMPTY);
            } else {
                slot1.markDirty();
            }

            if (stack2.getCount() == stack1.getCount()) {
                return ItemStack.EMPTY;
            }

            slot1.onTakeItem(player, stack2);
        }

        return stack1;
    }


    @Override
    public boolean canUse(PlayerEntity var1) {
        return true;
    }

    public boolean isRunning() {
        return this.propertyDelegate.get(0) > 0;
    }

    @Environment(EnvType.CLIENT)
    public int getOpTime() {
        int int_1 = this.propertyDelegate.get(2);
        int int_2 = this.propertyDelegate.get(3);
        return int_2 != 0 && int_1 != 0 ? int_1 * 24 / int_2 : 0;
    }

    @Environment(EnvType.CLIENT)
    public int getFuelProgress() {
        int int_1 = this.propertyDelegate.get(1);
        if (int_1 == 0) {
            int_1 = 200;
        }

        return this.propertyDelegate.get(0) * 13 / int_1;
    }
}
