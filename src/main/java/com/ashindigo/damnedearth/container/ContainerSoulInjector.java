package com.ashindigo.damnedearth.container;

import com.ashindigo.damnedearth.ItemSoulNeedle;
import com.ashindigo.damnedearth.blocks.DamnedEarthBlock;
import com.ashindigo.damnedearth.tileentity.SoulInjectorTileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.container.Slot;
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
        for (int i = 0; i < 3; ++i) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(inventory, x + i * 9 + 9, 8 + x * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }
        this.addProperties(propertyDelegate);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity playerEntity_1, int int_1) {
        ItemStack itemStack_1 = ItemStack.EMPTY;
        Slot slot_1 = this.slotList.get(int_1);
        if (slot_1 != null && slot_1.hasStack()) {
            ItemStack itemStack_2 = slot_1.getStack();
            itemStack_1 = itemStack_2.copy();
            if (int_1 == 2) {
                if (!this.insertItem(itemStack_2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot_1.onStackChanged(itemStack_2, itemStack_1);
            } else if (int_1 != 1 && int_1 != 0) {
                if (Block.getBlockFromItem(itemStack_2.getItem()) instanceof DamnedEarthBlock) {
                    if (!this.insertItem(itemStack_2, SoulInjectorTileEntity.earthSlot, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(itemStack_2)) {
                    if (!this.insertItem(itemStack_2, SoulInjectorTileEntity.fuelSlot, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemStack_2.getItem() instanceof ItemSoulNeedle) {
                    if (!this.insertItem(itemStack_2, SoulInjectorTileEntity.soulNeedleSlot, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (int_1 < 30) {
                    if (!this.insertItem(itemStack_2, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (int_1 < 39 && !this.insertItem(itemStack_2, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack_2, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack_2.isEmpty()) {
                slot_1.setStack(ItemStack.EMPTY);
            } else {
                slot_1.markDirty();
            }

            if (itemStack_2.getCount() == itemStack_1.getCount()) {
                return ItemStack.EMPTY;
            }

            slot_1.onTakeItem(playerEntity_1, itemStack_2);
        }

        return itemStack_1;
    }

    private boolean isFuel(ItemStack stack) {
        return AbstractFurnaceBlockEntity.createFuelTimeMap().containsKey(stack.getItem());
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
