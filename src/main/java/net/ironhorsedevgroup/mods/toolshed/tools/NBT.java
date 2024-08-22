package net.ironhorsedevgroup.mods.toolshed.tools;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class NBT {
    public static Integer getIntTag(ItemStack itemStack, String name) {
        try {
            return (int)itemStack.getTag().getDouble(name);
        } catch (Exception e) {
            return 0;
        }
    }

    public static Integer getIntTag(Entity entity, String name) {
        try {
            return (int)entity.getPersistentData().getDouble(name);
        } catch (Exception e) {
            return 0;
        }
    }

    public static ItemStack putIntTag(ItemStack itemStack, String name, Integer value) {
        if (value != 0) {
            itemStack.getOrCreateTag().putDouble(name, value);
        } else {
            removeTag(itemStack, name);
        }
        return itemStack;
    }

    public static Entity putIntTag(Entity entity, String name, Integer value) {
        if (value != 0) {
            entity.getPersistentData().putDouble(name, value);
        } else {
            removeTag(entity, name);
        }
        return entity;
    }

    public static Double getDoubleTag(ItemStack itemstack, String name) {
        try {
            return itemstack.getTag().getDouble(name);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public static Double getDoubleTag(Entity entity, String name) {
        try {
            return entity.getPersistentData().getDouble(name);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public static ItemStack putDoubleTag(ItemStack itemStack, String name, Double value) {
        if (value != 0) {
            itemStack.getOrCreateTag().putDouble(name, value);
        } else {
            removeTag(itemStack, name);
        }
        return itemStack;
    }

    public static Entity putDoubleTag(Entity entity, String name, Double value) {
        if (value != 0.0) {
            entity.getPersistentData().putDouble(name, value);
        } else {
            removeTag(entity, name);
        }
        return entity;
    }

    public static String getStringTag(ItemStack itemstack, String name) {
        try {
            return itemstack.getTag().getString(name);
        } catch (Exception e) {
            return "";
        }
    }

    public static ItemStack putStringTag(ItemStack itemStack, String name, String text) {
        if (text != "") {
            itemStack.getOrCreateTag().putString(name, text);
        } else {
            removeTag(itemStack, name);
        }
        return itemStack;
    }

    public static ItemStack appendStringTag(ItemStack itemStack, String name, String text) {
        String existingText = getStringTag(itemStack, name);
        putStringTag(itemStack, name, existingText + text);
        return itemStack;
    }

    public static ResourceLocation getLocationTag(ItemStack itemStack, String name) {
        try {
            return new ResourceLocation(itemStack.getTag().getString(name));
        } catch (Exception e) {
            return new ResourceLocation("null", "null");
        }
    }

    public static ItemStack putLocationTag(ItemStack itemStack, String name, ResourceLocation location) {
        if (location != null && !location.toString().equals("null:null")) {
            itemStack.getOrCreateTag().putString(name, location.toString());
        } else {
            removeTag(itemStack, name);
        }
        return itemStack;
    }

    public static Boolean getBoolTag(ItemStack itemStack, String name) {
        try {
            return itemStack.getTag().getBoolean(name);
        } catch (Exception e) {
            return false;
        }
    }

    public static ItemStack putBoolTag(ItemStack itemStack, String name, Boolean value) {
        if (value) {
            itemStack.getOrCreateTag().putBoolean(name, true);
        } else {
            removeTag(itemStack, name);
        }
        return itemStack;
    }

    public static ItemStack toggleBoolTag(ItemStack itemStack, String name) {
        if (getBoolTag(itemStack, name)) {
            itemStack.removeTagKey(name);
        } else {
            itemStack.getOrCreateTag().putBoolean(name, true);
        }
        return itemStack;
    }

    public static ItemStack removeTag(ItemStack itemStack, String name) {
        itemStack.removeTagKey(name);
        return itemStack;
    }

    public static Entity removeTag(Entity entity, String name) {
        entity.removeTag(name);
        return entity;
    }
}
