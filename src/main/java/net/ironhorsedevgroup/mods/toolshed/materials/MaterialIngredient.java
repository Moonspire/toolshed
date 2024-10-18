package net.ironhorsedevgroup.mods.toolshed.materials;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import org.jetbrains.annotations.Nullable;

public class MaterialIngredient extends AbstractIngredient {
    private final ResourceLocation material;

    public MaterialIngredient(String material) {
        this.material = new ResourceLocation(material);
    }

    public MaterialIngredient(ResourceLocation material) {
        this.material = material;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public JsonElement toJson() {
        return null;
    }

    public ResourceLocation getMaterial() {
        return material;
    }

    @Override
    public ItemStack[] getItems() {
        return Materials.getMaterial(material).getCrafting().getCraftingIngredient().getItems();
    }

    @Override
    public IntList getStackingIds() {
        return Materials.getMaterial(material).getCrafting().getCraftingIngredient().getStackingIds();
    }

    @Override
    public boolean isEmpty() {
        return Materials.getMaterial(material).getCrafting().getCraftingIngredient().isEmpty();
    }

    @Override
    public boolean isSimple() {
        return Materials.getMaterial(material).getCrafting().getCraftingIngredient().isSimple();
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        return Materials.getMaterial(material).getCrafting().getCraftingIngredient().test(stack);
    }

    public static class Serializer implements IIngredientSerializer<MaterialIngredient> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public MaterialIngredient parse(FriendlyByteBuf buffer) {
            return new MaterialIngredient(buffer.readResourceLocation());
        }

        @Override
        public MaterialIngredient parse(JsonObject json) {
            if (json.has("material")) {
                new MaterialIngredient(json.get("material").getAsString());
            }
            return new MaterialIngredient("toolshed:all");
        }

        @Override
        public void write(FriendlyByteBuf buffer, MaterialIngredient ingredient) {
            buffer.writeResourceLocation(ingredient.getMaterial());
        }
    }
}
