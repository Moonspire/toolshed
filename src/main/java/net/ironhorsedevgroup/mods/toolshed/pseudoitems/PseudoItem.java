package net.ironhorsedevgroup.mods.toolshed.pseudoitems;

import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@Deprecated(since = "1.0.4", forRemoval = true)
public class PseudoItem implements ItemLike {
    private ResourceLocation location;
    private ItemStack stack = new ItemStack(Items.APPLE);

    public PseudoItem(ResourceLocation location) {
        this.location = location;
    }

    public PseudoItem(Item item) {
        this.location = ForgeRegistries.ITEMS.getKey(item);
    }

    public PseudoItem putStringTag(String tag, String value) {
        NBT.putStringTag(stack, tag, value);
        return this;
    }

    public PseudoItem putIntTag(String tag, Integer value) {
        NBT.putIntTag(stack, tag, value);
        return this;
    }

    public PseudoItem putDoubleTag(String tag, Double value) {
        NBT.putDoubleTag(stack, tag, value);
        return this;
    }

    public PseudoItem putBoolTag(String tag, Boolean value) {
        NBT.putBoolTag(stack, tag, value);
        return this;
    }

    @Override
    public Item asItem() {
        if (ForgeRegistries.ITEMS.getValue(location) != null) {
            return ForgeRegistries.ITEMS.getValue(location);
        }
        return Items.AIR;
    }

    public ItemStack asItemStack() {
        ItemStack retStack = new ItemStack(this.asItem());
        for (String tag : this.stack.getOrCreateTag().getAllKeys()) {
            retStack.getOrCreateTag().put(tag, Objects.requireNonNull(this.stack.getOrCreateTag().get(tag)));
        }
        return retStack;
    }
}
