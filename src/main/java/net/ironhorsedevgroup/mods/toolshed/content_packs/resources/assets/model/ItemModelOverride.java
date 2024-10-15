package net.ironhorsedevgroup.mods.toolshed.content_packs.resources.assets.model;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface ItemModelOverride {
    BakedModel getModel(ItemStack stack);
    BakedModel getModel(Item item);
    BakedModel getModel();
}
