package net.ironhorsedevgroup.mods.toolshed.content_packs;

import net.ironhorsedevgroup.mods.toolshed.Toolshed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.RenderTypeGroup;

public class SimpleItemModelOverride implements ItemModelOverride {
    private final BakedModel model;

    public SimpleItemModelOverride() {
        this.model = Minecraft.getInstance().getModelManager().getMissingModel();
    }

    private SimpleItemModelOverride(BakedModel model) {
        this.model = model;
    }

    public static SimpleItemModelOverride fromLocation(ResourceLocation location) {
        location = new ResourceLocation(location.getNamespace(), "models/" + location.getPath() + ".json#inventory");
        try {
            BlockModel model = BlockModel.fromStream(Minecraft.getInstance().getResourceManager().openAsReader(location));
            return new SimpleItemModelOverride(new SimpleBakedModel.Builder(model, ItemOverrides.EMPTY, false).build(RenderTypeGroup.EMPTY));
        } catch (Exception ignored) {
            Toolshed.LOGGER.error("Failed to load model at {}", location);
            return new SimpleItemModelOverride();
        }
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
