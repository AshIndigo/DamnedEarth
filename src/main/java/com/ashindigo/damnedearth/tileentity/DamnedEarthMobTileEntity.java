package com.ashindigo.damnedearth.tileentity;

import com.ashindigo.damnedearth.DamnedEarth;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DamnedEarthMobTileEntity extends BlockEntity {

    private EntityType<?> mob = EntityType.PIG;

    public DamnedEarthMobTileEntity() {
        super(DamnedEarth.mobTEType);
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return new BlockEntityUpdateS2CPacket(this.pos, 2, this.toInitialChunkDataTag());
    }

    @Override
    public CompoundTag toInitialChunkDataTag() {
        return this.toTag(new CompoundTag());
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        mob = Registry.ENTITY_TYPE.get(Identifier.tryParse(tag.getString("mob")));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putString("mob", EntityType.getId(mob).toString());
        return tag;
    }

    public EntityType<?> getMob() {
        return mob;
    }

    public void setMob(EntityType<?> mob) {
        this.mob = mob;
    }
}
