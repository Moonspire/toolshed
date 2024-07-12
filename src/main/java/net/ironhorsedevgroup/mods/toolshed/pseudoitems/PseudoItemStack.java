package net.ironhorsedevgroup.mods.toolshed.pseudoitems;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

public class PseudoItemStack {
    public List<ItemLike> items = new ArrayList<>();

    public PseudoItemStack() {}

    public PseudoItemStack addItem(ItemLike item) {
        items.add(item);
        return this;
    }

    public PseudoItemStack addItem(Integer index, ItemLike item) {
        items.add(index, item);
        return this;
    }

    public ItemLike getItem() {
        for (ItemLike item : items) {
            if (item.asItem() != Items.AIR) {
                return item;
            }
        }
        return Items.AIR;
    }

    public ItemStack getItemStack() {
        for (ItemLike item : items) {
            if (item.asItem() != Items.AIR) {
                if (item instanceof PseudoItem pseudo) {
                    return pseudo.asItemStack();
                }
                return new ItemStack(item);
            }
        }
        return new ItemStack(Items.AIR);
    }
}
