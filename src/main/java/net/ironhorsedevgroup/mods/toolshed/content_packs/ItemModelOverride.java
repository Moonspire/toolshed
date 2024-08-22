package net.ironhorsedevgroup.mods.toolshed.content_packs;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;

public interface ItemModelOverride {
    BakedModel getModel(ItemStack stack);
}
