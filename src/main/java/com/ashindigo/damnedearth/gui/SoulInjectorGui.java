package com.ashindigo.damnedearth.gui;

import com.ashindigo.damnedearth.DamnedEarth;
import com.ashindigo.damnedearth.container.ContainerSoulInjector;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class SoulInjectorGui<T extends Container> extends AbstractContainerScreen<T> {

    public SoulInjectorGui(T container_1, PlayerInventory playerInventory_1) {
        super(container_1, playerInventory_1, new TranslatableText("container.damnedearth.soulinjector"));
    }

    @Override
    protected void drawBackground(float var1, int var2, int var3) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Objects.requireNonNull(this.minecraft).getTextureManager().bindTexture(new Identifier(DamnedEarth.MODID, "textures/gui/soulinjector.png"));
        int left = this.left;
        int top = this.top;
        this.blit(left, top, 0, 0, this.containerWidth, this.containerHeight);
        if(container instanceof ContainerSoulInjector) {
            if (((ContainerSoulInjector) this.container).isRunning()) {
                int int_6 = ((ContainerSoulInjector) this.container).getFuelProgress();
                this.blit(left + 56, top + 36 + 12 - int_6, 176, 12 - int_6, 14, int_6 + 1);
            }
            this.blit(left + 79, top + 34, 176, 14, ((ContainerSoulInjector) this.container).getOpTime() + 1, 16);
        }
    }

    @Override
    public void render(int int_1, int int_2, float float_1) {
        this.renderBackground();
        super.render(int_1, int_2, float_1);
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.drawMouseoverTooltip(int_1, int_2);
    }

    @Override
    protected void drawForeground(int x, int y) {
        this.font.draw(this.title.asFormattedString(), (float)(this.containerWidth / 2 - this.font.getStringWidth(this.title.asFormattedString()) / 2), 6.0F, 4210752);
        this.font.draw(this.playerInventory.getDisplayName().asFormattedString(), 8.0F, (float)(this.containerHeight - 96 + 2), 4210752);
    }
}
