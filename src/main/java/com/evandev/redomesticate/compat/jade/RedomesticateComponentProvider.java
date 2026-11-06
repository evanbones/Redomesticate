package com.evandev.redomesticate.compat.jade;

import com.evandev.redomesticate.Constants;
import com.evandev.redomesticate.LangDefinition;
import com.evandev.redomesticate.util.TameableUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum RedomesticateComponentProvider implements IEntityComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        if (entityAccessor.getEntity() instanceof LivingEntity livingEntity) {
            var enchants = TameableUtils.getEnchantDescriptions(livingEntity);
            var hasPetbed = TameableUtils.getPetBedPos(livingEntity);
            if (hasPetbed != null) {
                iTooltip.add(Component.translatable(LangDefinition.has_pet_bed_at_pos, hasPetbed.toShortString()).withStyle(ChatFormatting.RED));
            }
            if (enchants.size() > 1) {
                for (Component enchant : enchants) {
                    iTooltip.add(enchant);
                }
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "collar_tag");
    }
}