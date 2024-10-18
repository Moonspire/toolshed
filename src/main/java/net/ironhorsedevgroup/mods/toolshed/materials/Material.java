package net.ironhorsedevgroup.mods.toolshed.materials;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.toolshed.network.stc.MaterialPacket;
import net.ironhorsedevgroup.mods.toolshed.tools.Color;
import net.ironhorsedevgroup.mods.toolshed.tools.Data;
import net.ironhorsedevgroup.mods.toolshed.tools.Tags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class Material {
    private final Properties properties;
    private final Crafting crafting;
    private final String requires;

    public Material() {
        this.properties = new Properties();
        this.crafting = new Crafting();
        this.requires = "";
    }

    private Material(Properties properties, Crafting crafting, String requires) {
        this.properties = properties;
        this.crafting = crafting;
        this.requires = requires;
    }

    public static Material fromJson(JsonObject json) {
        String requires = "";
        if (json.has("requires")) {
            requires = json.get("requires").getAsString();
        }
        Data.DataObject data = new Data.DataObject(json);
        Properties properties = Properties.fromData(data.getObject("properties"));
        Crafting crafting = Crafting.fromData(data.getObject("crafting"));

        return new Material(properties, crafting, requires);
    }

    public static Material fromPacket(MaterialPacket packet) {
        Properties properties = Properties.fromPacket(packet);
        Crafting crafting = Crafting.fromPacket(packet);

        return new Material(properties, crafting, "");
    }

    public String getRequirement() {
        return requires;
    }

    public Properties getProperties() {
        return properties;
    }

    public Crafting getCrafting() {
        return crafting;
    }

    public static class Properties {
        private final int color;
        private final boolean flammable;
        private final int density;
        private final byte hardness;
        private final byte purity;

        private Properties() {
            this.color = 0;
            this.flammable = false;
            this.density = 1;
            this.hardness = 1;
            this.purity = 1;
        }

        private Properties(int color, boolean flammable, int density, byte hardness, byte purity) {
            this.color = color;
            this.flammable = flammable;
            this.density = density;
            this.hardness = hardness;
            this.purity = purity;
        }

        public static Properties fromJson(JsonObject json) {
            int color = 0;
            boolean flammable = false;
            int density = 0;
            byte hardness = 0;
            byte purity = 0;

            if (json.has("color")) {
                JsonArray colorArray = json.getAsJsonArray("color");
                color = Color.getIntFromRGB(colorArray.get(0).getAsInt(), colorArray.get(1).getAsInt(), colorArray.get(2).getAsInt());
            }
            if (json.has("flammable")) {
                flammable = json.get("flammable").getAsBoolean();
            }
            if (json.has("density")) {
                density = json.get("density").getAsInt();
            }
            if (json.has("hardness")) {
                hardness = json.get("hardness").getAsByte();
            }
            if (json.has("purity")) {
                purity = json.get("purity").getAsByte();
            }

            return new Properties(color, flammable, density, hardness, purity);
        }

        public static Properties fromData(Data.DataObject data) {
            return fromJson(data.get().getAsJsonObject());
        }


        public static Properties fromPacket(MaterialPacket packet) {
            return new Properties(
                    packet.color,
                    false,
                    1,
                    (byte) 1,
                    (byte) 1
            );
        }

        public int getColor() {
            return color;
        }

        public boolean isFlammable() {
            return flammable;
        }

        public byte getHardness() {
            return hardness;
        }

        public byte getPurity() {
            return purity;
        }

        public int getDensity() {
            return density;
        }
    }

    public static class Crafting {
        private final Ingredient ingredient;
        private final ResourceLocation castingFluid;
        private final boolean castable;

        public Crafting() {
            this.ingredient = Ingredient.EMPTY;
            this.castingFluid = null;
            this.castable = false;
        }

        private Crafting(Ingredient ingredient, ResourceLocation castingFluid, boolean castable) {
            this.ingredient = ingredient;
            this.castingFluid = castingFluid;
            this.castable = castable;
        }

        public static Crafting fromJson(JsonObject json) {
            Ingredient ingredient = Ingredient.EMPTY;
            ResourceLocation castingFluid = null;
            boolean castable = false;

            if (json.has("crafting_item")) {
                JsonObject craftingItem = json.getAsJsonObject("crafting_item");

                String type = "item";
                ResourceLocation craftingLocation = null;

                if (craftingItem.has("type")) {
                    type = craftingItem.get("type").getAsString();
                }

                craftingLocation = new ResourceLocation(craftingItem.get("ingredient").getAsString());

                if (Objects.equals(type, "item")) {
                    ingredient = Ingredient.of(ForgeRegistries.ITEMS.getValue(craftingLocation));
                } else if (Objects.equals(type, "tag")) {
                    ingredient = Ingredient.of(Tags.getItemTag(craftingLocation));
                }
            } else if (json.has("crafting_ingredient")) {
                ingredient = Ingredient.fromJson(json.getAsJsonObject("crafting_ingredient"));
            }

            if (json.has("castable")) {
                castable = json.get("castable").getAsBoolean();
            }

            if (castable || json.has("casting_fluid")) {
                castingFluid = new ResourceLocation(json.get("casting_fluid").getAsString());
            }

            return new Crafting(ingredient, castingFluid, castable);
        }

        public static Crafting fromData(Data.DataObject data) {
            return fromJson(data.get().getAsJsonObject());
        }

        public static Crafting fromPacket(MaterialPacket packet) {
            Ingredient ingredient = packet.ingredient;
            return new Crafting(ingredient, null, false);
        }

        public Ingredient getCraftingIngredient() {
            return ingredient;
        }

        public ResourceLocation getCastingFluid() {
            return castingFluid;
        }

        public boolean isCastable() {
            return castable;
        }
    }
}
