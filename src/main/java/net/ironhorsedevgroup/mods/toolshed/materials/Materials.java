package net.ironhorsedevgroup.mods.toolshed.materials;

import net.ironhorsedevgroup.mods.toolshed.Toolshed;
import net.ironhorsedevgroup.mods.toolshed.content_packs.resources.assets.ResourceLoader;
import net.ironhorsedevgroup.mods.toolshed.content_packs.resources.data.DataLoader;
import net.ironhorsedevgroup.mods.toolshed.network.ToolshedMessages;
import net.ironhorsedevgroup.mods.toolshed.network.stc.MaterialPacket;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;

import java.util.*;

public class Materials {
    private final Map<ResourceLocation, Material> materials = new HashMap<>();
    private Ingredient materialIngredient = Ingredient.EMPTY;
    public static final Material NULL = new Material();
    private static final List<ResourceLocation> erroredMaterials = new ArrayList<>();

    public void loadMaterials(List<ResourceLocation> materials, MinecraftServer server) {
        clearMaterials();
        for (ResourceLocation material : materials) {
            loadMaterial(material, server);
        }
    }

    public void loadMaterial(ResourceLocation location, MinecraftServer server) {
        Material material = Material.fromJson(DataLoader.loadJson(location, server));
        if (!Objects.equals(material.getRequirement(), "")) {
            if (!ModList.get().isLoaded(material.getRequirement())) {
                return;
            }
        }
        String[] strippedPath = location.getPath().split("/");
        location = new ResourceLocation(location.getNamespace(), strippedPath[strippedPath.length - 1]);
        Toolshed.LOGGER.info("Registering server material: {}", location);
        updateMaterial(location, material);
    }

    public void loadMaterial(MaterialPacket packet) {
        ResourceLocation location = packet.location;
        Toolshed.LOGGER.info("Registering client material: {}", location);
        updateMaterial(location, Material.fromPacket(packet));
    }

    public Ingredient createIngredient() {
        List<ItemStack> items = new ArrayList<>();
        for (Material material : materials.values()) {
            items.addAll(Arrays.stream(material.getCrafting().getCraftingIngredient().getItems()).toList());
        }
        return Ingredient.of(items.stream());
    }

    public Ingredient getIngredient() {
        if (materialIngredient == Ingredient.EMPTY) {
            materialIngredient = createIngredient();
        }
        return materialIngredient;
    }

    public void clearMaterials() {
        materials.clear();
        erroredMaterials.clear();
    }

    public void clearMaterial(ResourceLocation location) {
        materials.remove(location);
        erroredMaterials.remove(location);
    }

    public void updateMaterial(ResourceLocation location, Material material) {
        materials.remove(location);
        materials.put(location, material);
    }

    public void sendMaterials(ServerPlayer player) {
        for (ResourceLocation material : materials.keySet()) {
            ToolshedMessages.sendToPlayer(new MaterialPacket(material, materials.get(material)), player);
        }
    }

    public boolean hasMaterial(ResourceLocation location) {
        return materials.containsKey(location);
    }

    public static Material getMaterial(String namespace, String path) {
        return getMaterial(new ResourceLocation(namespace, path));
    }

    public static Material getMaterial(String location) {
        return getMaterial(new ResourceLocation(location));
    }

    public static Material getMaterial(ResourceLocation location) {
        if (DataLoader.getMaterials().hasMaterial(location)) {
            return DataLoader.getMaterials().getMaterial(location, false);
        }
        return ResourceLoader.getMaterials().getMaterial(location, true);
    }

    public Material getMaterial(ResourceLocation location, boolean client) {
        if (!Objects.equals(location, null) && !erroredMaterials.contains(location)) {
            if (materials.containsKey(location)) {
                return materials.get(location);
            }
            Toolshed.LOGGER.error("Could not locate material: {}", location);
            erroredMaterials.add(location);
        }
        return NULL;
    }

    public ResourceLocation getMaterial(ItemStack itemStack) {
        if (!NBT.getStringTag(itemStack, "material").equals("")) {
            return NBT.getLocationTag(itemStack, "material");
        }
        for (ResourceLocation key : materials.keySet()) {
            Material material = getMaterial(key);
            if (material.getCrafting().getCraftingIngredient().test(itemStack)) {
                return key;
            }
        }
        return new ResourceLocation("null");
    }

    public static List<ResourceLocation> getMaterials() {
        if (DataLoader.getMaterials().isEmpty()) {
            return ResourceLoader.getMaterials().getMaterials(true);
        }
        return DataLoader.getMaterials().getMaterials(false);
    }

    public List<ResourceLocation> getMaterials(boolean client) {
        return materials.keySet().stream().toList();
    }

    public boolean isEmpty() {
        return materials.isEmpty();
    }

    public String getMaterialLang(ResourceLocation location) {
        if (!Objects.equals(location, null) && materials.containsKey(location)) {
            return Toolshed.MODID + ".material." + location.getNamespace() + "." + location.getPath();
        }
        return Toolshed.MODID + ".material.null";
    }
}
