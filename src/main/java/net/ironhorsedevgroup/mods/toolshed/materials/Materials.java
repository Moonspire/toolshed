package net.ironhorsedevgroup.mods.toolshed.materials;

import net.ironhorsedevgroup.mods.toolshed.Toolshed;
import net.ironhorsedevgroup.mods.toolshed.content_packs.resources.data.DataLoader;
import net.ironhorsedevgroup.mods.toolshed.network.ToolshedMessages;
import net.ironhorsedevgroup.mods.toolshed.network.stc.MaterialColorPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;

import java.util.*;

public class Materials {
    private static final Map<ResourceLocation, Material> materials = new HashMap<>();
    private static final Map<ResourceLocation, Material> clientMaterials = new HashMap<>();
    private static final List<ResourceLocation> erroredMaterials = new ArrayList<>();

    public static void loadMaterials(List<ResourceLocation> materials, MinecraftServer server) {
        clearMaterials();
        for (ResourceLocation material : materials) {
            loadMaterial(material, server);
        }
    }

    public static void loadMaterial(ResourceLocation location, MinecraftServer server) {
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

    public static List<ResourceLocation> getMaterials() {
        return materials.keySet().stream().toList();
    }

    public static void clearMaterials() {
        materials.clear();
        clientMaterials.clear();
        erroredMaterials.clear();
    }

    public static void clearMaterial(ResourceLocation location) {
        materials.remove(location);
        clientMaterials.remove(location);
        erroredMaterials.remove(location);
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

    @OnlyIn(Dist.CLIENT)
    public static void loadMaterial(MaterialColorPacket packet) {
        ResourceLocation location = packet.location;
        Toolshed.LOGGER.info("Registering client material: {}", location);
        updateClientMaterial(location, Material.fromPacket(packet));
    }

    @OnlyIn(Dist.CLIENT)
    public static List<ResourceLocation> getClientMaterials() {
        return clientMaterials.keySet().stream().toList();
    }

    @OnlyIn(Dist.CLIENT)
    public static void updateClientMaterial(ResourceLocation location, Material material) {
        clientMaterials.remove(location);
        clientMaterials.put(location, material);
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean hasClientMaterial(ResourceLocation location) {
        return clientMaterials.containsKey(location);
    }

    @OnlyIn(Dist.CLIENT)
    public static Material getClientMaterial(String namespace, String path) {
        return getClientMaterial(new ResourceLocation(namespace, path));
    }

    @OnlyIn(Dist.CLIENT)
    public static Material getClientMaterial(String location) {
        if (!Objects.equals(location, null)) {
            return getClientMaterial(new ResourceLocation(location));
        }
        return getNull();
    }

    @OnlyIn(Dist.CLIENT)
    public static Material getClientMaterial(ResourceLocation location) {
        if (!Objects.equals(location, null) && !erroredMaterials.contains(location)) {
            if (clientMaterials.containsKey(location)) {
                return clientMaterials.get(location);
            }
            Toolshed.LOGGER.error("Could not locate material: {}", location);
            erroredMaterials.add(location);
        }
        return getNull();
    }

    @OnlyIn(Dist.CLIENT)
    public static String getMaterialLang(ResourceLocation location) {
        if (!Objects.equals(location, null) && clientMaterials.containsKey(location)) {
            return Toolshed.MODID + ".material." + location.getNamespace() + "." + location.getPath();
        }
        return Toolshed.MODID + ".material.null";
    }
}
