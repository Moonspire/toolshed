package net.ironhorsedevgroup.mods.toolshed.pseudoitems;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class PseudoItemStack {
    private final ResourceLocation id;
    private final Map<String, Object> nbt = new HashMap<>();

    public PseudoItemStack(ResourceLocation id) {
        this.id = id;
    }

    public PseudoItemStack(String id) {
        this.id = new ResourceLocation(id);
    }

    public ResourceLocation getResourceLocation() {
        return id;
    }

    public PseudoItemStack addNBT(String tagName, String tagData) {
        this.nbt.put(tagName, tagData);
        return this;
    }

    public PseudoItemStack addNBT(String tagName, Double tagData) {
        this.nbt.put(tagName, tagData);
        return this;
    }

    public PseudoItemStack addNBT(String tagName, Integer tagData) {
        this.nbt.put(tagName, tagData);
        return this;
    }

    public PseudoItemStack addNBT(String tagName, Boolean tagData) {
        this.nbt.put(tagName, tagData);
        return this;
    }

    public Map<String, Object> getNBT() {
        return this.nbt;
    }

    public Object getNBT(String tagName) {
        return this.nbt.get(tagName);
    }
}
