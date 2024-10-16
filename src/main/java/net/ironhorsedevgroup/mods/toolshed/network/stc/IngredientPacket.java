package net.ironhorsedevgroup.mods.toolshed.network.stc;

import net.ironhorsedevgroup.mods.toolshed.materials.Materials;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class IngredientPacket {
    public Ingredient ingredient;

    public IngredientPacket(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public static IngredientPacket decode(FriendlyByteBuf buf) {
        return new IngredientPacket(Ingredient.fromNetwork(buf));
    }

    public void encode(FriendlyByteBuf buf) {
        ingredient.toNetwork(buf);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Materials.loadIngredient(this);
        });
        return true;
    }
}
