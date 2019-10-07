package com.ashindigo.damnedearth;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

public class ItemSoulNeedle extends Item {

    ItemSoulNeedle(Settings item$Settings_1) {
        super(item$Settings_1);
        this.addPropertyGetter(new Identifier(DamnedEarth.MODID, "filled"), (stack, world, entity) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity instanceof PlayerEntity && stack.hasTag() && stack.getOrCreateTag().containsKey("mob") ? 1.0F : 0.0F;
            }
        });
    }

    @Override
    public TypedActionResult<ItemStack> use(World world_1, PlayerEntity playerEntity_1, Hand hand_1) {
        if (playerEntity_1.isSneaking()) {
            playerEntity_1.getStackInHand(hand_1).setTag(null);
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, playerEntity_1.getStackInHand(hand_1));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity mob, LivingEntity player) {
        if (mob instanceof HostileEntity) {
            if (!MobBlacklist.contains(mob.getType())) {
                CompoundTag tag = new CompoundTag();
                tag.putString("mob", Registry.ENTITY_TYPE.getId(mob.getType()).toString());
                stack.setTag(tag);
                return true;
            }
        }
        return false;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack itemStack_1, World world_1, List<Text> list_1, TooltipContext tooltipContext_1) {
        if (itemStack_1.getTag() != null) {
            list_1.add(new TranslatableText(Registry.ENTITY_TYPE.get(Identifier.tryParse(itemStack_1.getTag().getString("mob"))).getTranslationKey()));
        }
    }
}

