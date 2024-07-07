package net.ironhorsedevgroup.mods.toolshed.pseudoitems;

import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PseudoItem {
    private final List<PseudoItemStack> orderedItems = new ArrayList<>();
    private ItemStack resolvedItem = null;

    public PseudoItem(String... items) {
        for (String item : items) {
            this.orderedItems.add(new PseudoItemStack(item));
        }
    }

    public ItemStack resolveItem() {
        if (resolvedItem == null) {
            for (PseudoItemStack item : this.orderedItems) {
                ResourceLocation location = item.getResourceLocation();
                if (ModList.get().isLoaded(location.getNamespace())) {
                    if (ForgeRegistries.ITEMS.containsKey(location)) {
                        ItemStack retStack = new ItemStack(ForgeRegistries.ITEMS.getValue(location));
                        for (Map.Entry<String, Object> entry : item.getNBT().entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            if (value instanceof Boolean bool) {
                                NBT.putBoolTag(retStack, key, bool);
                            } else if (value instanceof Double num) {
                                NBT.putDoubleTag(retStack, key, num);
                            } else if (value instanceof Integer num) {
                                NBT.putIntTag(retStack, key, num);
                            } else if (value instanceof String str) {
                                NBT.putStringTag(retStack, key, str);
                            }
                            resolvedItem = retStack;
                            return retStack;
                        }
                    }
                }
            }
            return new ItemStack(Items.AIR);
        }
        return resolvedItem;
    }
}
