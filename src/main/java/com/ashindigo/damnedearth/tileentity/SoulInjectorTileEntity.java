package com.ashindigo.damnedearth.tileentity;

import com.ashindigo.damnedearth.DamnedEarth;
import com.ashindigo.damnedearth.SoulInjectorRecipe;
import com.ashindigo.damnedearth.blocks.SoulInjectorBlock;
import com.ashindigo.damnedearth.container.ContainerSoulInjector;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.Container;
import net.minecraft.container.NameableContainerProvider;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;


// TODO Check hopper behavior
public class SoulInjectorTileEntity extends BlockEntity implements NameableContainerProvider, InventoryProvider, Tickable {

    private final PropertyDelegate propertyDelegate;
    private final SoulInjectorInventory inventory = new SoulInjectorInventory();
    private int burnTime;
    private int cookTime;
    private final int cookTimeTotal = 200;
    private int fuelTime;

    private static final int fuelSlot = 0;
    public static final int soulNeedleSlot = 1;
    private static final int earthSlot = 2;
    private static final int mobBlockSlot = 3;

    public SoulInjectorTileEntity() {
        super(DamnedEarth.soulInjectorType);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int mode) {
                switch (mode) {
                    case 0:
                        return burnTime;
                    case 1:
                        return fuelTime;
                    case 2:
                        return cookTime;
                    case 3:
                        return cookTimeTotal;
                    default:
                        return 0;
                }
            }

            public void set(int mode, int numb) {
                switch (mode) {
                    case 0:
                        burnTime = numb;
                        break;
                    case 1:
                        fuelTime = numb;
                        break;
                    case 2:
                        cookTime = numb;
                        break;
                }
            }

            @Override
            public int size() {
                return 4;
            }
        };
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("container.damnedearth.soulinjector");
    }

    @Override
    public Container createMenu(int var1, PlayerInventory var2, PlayerEntity var3) {
        return new ContainerSoulInjector(var1, var2, this, propertyDelegate);
    }

    @Override
    public void fromTag(CompoundTag compound) {
        super.fromTag(compound);
        this.burnTime = compound.getShort("BurnTime");
        this.cookTime = compound.getShort("CookTime");
        this.fuelTime = this.getFuelTime(this.inventory.getInvStack(1));
        if (compound.containsKey("inv")) {
            ListTag listTag = compound.getList("inv", 10);
            for (int i = 0; i < listTag.size(); i++) {
                inventory.setInvStack(i, ItemStack.fromTag(listTag.getCompoundTag(i)));
            }
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag compound) {
        compound.putShort("BurnTime", (short)this.burnTime);
        compound.putShort("CookTime", (short)this.cookTime);
        ListTag listTag = new ListTag();
        for (int i = 0; i < inventory.getInvSize(); i++) {
            listTag.add(i, inventory.getInvStack(i).toTag(new CompoundTag()));
        }
        compound.put("inv", listTag);
        return super.toTag(compound);
    }

    @Override
    public void tick() {
        boolean burning = this.isBurning();
        boolean running = false;
        if (this.isBurning()) {
            --this.burnTime;
        }

        //if (world != null && !world.isClient) {
            ItemStack itemStack = this.inventory.getInvStack(fuelSlot);
            if (!this.isBurning() && (itemStack.isEmpty() || this.inventory.getInvStack(earthSlot).isEmpty()) && !inventory.getInvStack(soulNeedleSlot).isEmpty()) {
                if (!this.isBurning() && this.cookTime > 0) {
                    this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
                }
            } else {
                SoulInjectorRecipe recipe = new SoulInjectorRecipe();
                if (!this.isBurning() && this.canAcceptRecipeOutput(recipe) && !inventory.getInvStack(soulNeedleSlot).isEmpty()) {
                    this.burnTime = this.getFuelTime(itemStack);
                    this.fuelTime = this.burnTime;
                    if (this.isBurning()) {
                        running = true;
                        if (!itemStack.isEmpty()) {
                            Item item = itemStack.getItem();
                            itemStack.decrement(1);
                            if (itemStack.isEmpty()) {
                                Item item_2 = item.getRecipeRemainder();
                                this.inventory.setInvStack(fuelSlot, item_2 == null ? ItemStack.EMPTY : new ItemStack(item_2));
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canAcceptRecipeOutput(recipe) && !inventory.getInvStack(soulNeedleSlot).isEmpty()) {
                    ++this.cookTime;
                    if (this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.craftRecipe(recipe);
                        running = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            }

            if (burning != this.isBurning()) {
                running = true;
                if (this.world != null) {
                    this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(SoulInjectorBlock.LIT, this.isBurning()), 3);
                }
            }
        //}

        if (running) {
            this.markDirty();
        }
    }

    private void craftRecipe(SoulInjectorRecipe recipe_1) {
        if (recipe_1 != null && this.canAcceptRecipeOutput(recipe_1)) {
            ItemStack stack = this.inventory.getInvStack(earthSlot);
            ItemStack stack1 = recipe_1.craft(inventory);
            ItemStack stack2 = this.inventory.getInvStack(mobBlockSlot);
            if (stack2.isEmpty()) {
                this.inventory.setInvStack(mobBlockSlot, stack1.copy());
            } else if (stack2.getItem() == stack1.getItem()) {
                stack2.increment(1);
            }
            stack.decrement(1);
        }
    }

    private int getFuelTime(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        } else {
            return AbstractFurnaceBlockEntity.createFuelTimeMap().getOrDefault(stack.getItem(), 0);
        }
    }

    private boolean canAcceptRecipeOutput(SoulInjectorRecipe recipe) {
        if (!this.inventory.getInvStack(earthSlot).isEmpty() && recipe != null) {
            ItemStack stack1 = recipe.craft(inventory);
            if (stack1.isEmpty()) {
                return false;
            } else {
                ItemStack stack2 = this.inventory.getInvStack(mobBlockSlot);
                if (stack2.isEmpty()) {
                    return true;
                } else if (!stack2.isItemEqualIgnoreDamage(stack1)) {
                    return false;
                } else if (!ItemStack.areTagsEqual(stack1, stack2)) {
                    return false;
                } else if (stack2.getCount() < inventory.getInvMaxStackAmount() && stack2.getCount() < stack2.getMaxCount()) {
                    return true;
                } else {
                    return stack2.getCount() < stack1.getMaxCount();
                }
            }
        } else {
            return false;
        }
    }

    private boolean isBurning() {
        return burnTime > 0;
    }


    // Params can be null
    @Override
    public SidedInventory getInventory(BlockState var1, IWorld var2, BlockPos var3) {
        return inventory;
    }

    private static class SoulInjectorInventory extends BasicInventory implements SidedInventory {

        SoulInjectorInventory() {
          super(4);
        }

        @Override
        public int[] getInvAvailableSlots(Direction var1) {
            return new int[4];
        }

        @Override
        public boolean canInsertInvStack(int var1, ItemStack var2, Direction var3) {
            return true;
        }

        @Override
        public boolean canExtractInvStack(int var1, ItemStack var2, Direction var3) {
            return true;
        }
    }
}
