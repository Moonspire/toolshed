package net.ironhorsedevgroup.mods.toolshed.network.stc;

import net.ironhorsedevgroup.mods.toolshed.content_packs.resources.assets.ResourceLoader;
import net.ironhorsedevgroup.mods.toolshed.materials.Material;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MaterialPacket {
    public ResourceLocation location;
    public int color;
    public Ingredient ingredient;

    public MaterialPacket(ResourceLocation location, Material material) {
        this.location = location;
        color = material.getProperties().getColor();
        ingredient = material.getCrafting().getCraftingIngredient();
    }

    public MaterialPacket(ResourceLocation location, int color, Ingredient ingredient) {
        this.location = location;
        this.color = color;
        this.ingredient = ingredient;
    }

    public static MaterialPacket decode(FriendlyByteBuf buf) {
        ResourceLocation location = buf.readResourceLocation();
        int color = buf.readInt();
        return new MaterialPacket(location, color, Ingredient.fromNetwork(buf));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(location);
        buf.writeInt(color);
        ingredient.toNetwork(buf);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ResourceLoader.getMaterials().loadMaterial(this);
        });
        return true;
    }
}
