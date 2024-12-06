package net.ironhorsedevgroup.mods.toolshed.tools;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class Tags {
    public static TagKey<Item> getItemTag(ResourceLocation tag) {
        var tagManager = ForgeRegistries.ITEMS.tags();
        return tagManager.createTagKey(tag);
    }

    public static TagKey<Item> getItemTag(String tag) {
        return getItemTag(new ResourceLocation(tag));
    }
}
