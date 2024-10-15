package net.ironhorsedevgroup.mods.toolshed.content_packs.resources.assets.model;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ItemModelOverrides {
    private static final Map<Item, ItemModelOverride> models = new HashMap<>();

    public static void registerItem(Item item, ItemModelOverride override) {
        if (!isOverwrote(item)) {
            models.put(item, override);
        }
    }

    public static boolean isOverwrote(Item item) {
        return models.containsKey(item);
    }

    public static BakedModel getModel(ItemStack stack) {
        return models.get(stack.getItem()).getModel(stack);
    }
}
