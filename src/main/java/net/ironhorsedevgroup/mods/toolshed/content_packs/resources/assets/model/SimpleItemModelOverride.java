package net.ironhorsedevgroup.mods.toolshed.content_packs.resources.assets.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;


public class SimpleItemModelOverride implements ItemModelOverride {
    private final BakedModel model;

    public SimpleItemModelOverride() {
        this.model = Minecraft.getInstance().getModelManager().getMissingModel();
    }

    private SimpleItemModelOverride(BakedModel model) {
        this.model = model;
    }

    public static SimpleItemModelOverride fromLocation(ResourceLocation location) {
        ModelManager manager = Minecraft.getInstance().getModelManager();
        location = new ModelResourceLocation(location, "inventory");
        return new SimpleItemModelOverride(manager.getModel(location));

        //CompositeModel.Baked.Builder builder = CompositeModel.Baked.builder(false, true, false, model.getMaterial("particle").sprite(), ItemOverrides.EMPTY, model.getTransforms());
    }

    @Override
    public BakedModel getModel(ItemStack stack) {
        return model;
    }

    @Override
    public BakedModel getModel(Item item) {
        return model;
    }

    @Override
    public BakedModel getModel() {
        return model;
    }
}
