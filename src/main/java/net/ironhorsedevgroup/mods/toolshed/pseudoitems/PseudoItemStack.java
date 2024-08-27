package net.ironhorsedevgroup.mods.toolshed.pseudoitems;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

@Deprecated(since = "1.0.4", forRemoval = true)
public class PseudoItemStack implements ItemLike {
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

    @Override
    public Item asItem() {
        for (ItemLike item : items) {
            if (item.asItem() != Items.AIR) {
                return item.asItem();
            }
        }
        return Items.AIR;
    }

    public ItemStack asItemStack() {
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
