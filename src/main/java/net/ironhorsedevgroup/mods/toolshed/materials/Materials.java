package net.ironhorsedevgroup.mods.toolshed.materials;

import net.ironhorsedevgroup.mods.toolshed.Toolshed;
import net.ironhorsedevgroup.mods.toolshed.content_packs.resources.data.DataLoader;
import net.ironhorsedevgroup.mods.toolshed.network.ToolshedMessages;
import net.ironhorsedevgroup.mods.toolshed.network.stc.MaterialColorPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

public class Materials {
    private static final Map<ResourceLocation, Material> materials = new HashMap<>();
    private static final List<ResourceLocation> erroredMaterials = new ArrayList<>();

    public static void loadMaterials(List<ResourceLocation> materials, MinecraftServer server) {
        clearMaterials();
        for (ResourceLocation material : materials) {
            loadMaterial(material, server);
        }
    }

    public static void loadMaterial(ResourceLocation location, MinecraftServer server) {
        Material material = Material.fromJson(DataLoader.loadJson(location, server));
        String[] strippedPath = location.getPath().split("/");
        location = new ResourceLocation(location.getNamespace(), strippedPath[strippedPath.length - 1]);
        Toolshed.LOGGER.info("Registering server material: {}", location);
        updateMaterial(location, material);
    }

    public static void loadMaterial(MaterialColorPacket packet) {
        ResourceLocation location = packet.location;
        Toolshed.LOGGER.info("Registering client material: {}", location);
        updateMaterial(location, Material.fromPacket(packet));
    }

    public static List<ResourceLocation> getMaterials() {
        return materials.keySet().stream().toList();
    }

    public static void clearMaterials() {
        materials.clear();
    }

    public static void clearMaterial(ResourceLocation location) {
        materials.remove(location);
    }

    public static void updateMaterial(ResourceLocation location, Material material) {
        materials.remove(location);
        materials.put(location, material);
    }

    public static void sendMaterials(ServerPlayer player) {
        for (ResourceLocation material : materials.keySet()) {
            ToolshedMessages.sendToPlayer(new MaterialColorPacket(material, materials.get(material)), player);
        }
    }

    public static Material getNull() {
        return new Material();
    }

    public static boolean hasMaterial(ResourceLocation location) {
        return materials.containsKey(location);
    }

    public static Material getMaterial(String namespace, String path) {
        return getMaterial(new ResourceLocation(namespace, path));
    }

    public static Material getMaterial(String location) {
        if (!Objects.equals(location, null)) {
            return getMaterial(new ResourceLocation(location));
        }
        return getNull();
    }

    public static Material getMaterial(ResourceLocation location) {
        if (!Objects.equals(location, null) && !erroredMaterials.contains(location)) {
            if (materials.containsKey(location)) {
                return materials.get(location);
            }
            Toolshed.LOGGER.error("Could not locate material: {}", location);
            erroredMaterials.add(location);
        }
        return getNull();
    }

    public static String getMaterialLang(ResourceLocation location) {
        if (!Objects.equals(location, null) && materials.containsKey(location)) {
            return Toolshed.MODID + ".material." + location.getNamespace() + "." + location.getPath();
        }
        return Toolshed.MODID + ".material.null";
    }
}
