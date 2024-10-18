package net.ironhorsedevgroup.mods.toolshed.content_packs.resources.assets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.ironhorsedevgroup.mods.toolshed.Toolshed;
import net.ironhorsedevgroup.mods.toolshed.materials.Materials;
import net.ironhorsedevgroup.mods.toolshed.tools.Data;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ResourceLoader {
    private static final Map<String, ResourceFileHandler> handlers = new HashMap<>();
    @OnlyIn(Dist.CLIENT)
    private static final Materials materials = new Materials();

    public static void addPackAssetHandler(String fileName, ResourceFileHandler handler) {
        handlers.put(fileName, handler);
    }

    @OnlyIn(Dist.CLIENT)
    public static Materials getMaterials() {
        return materials;
    }

    @OnlyIn(Dist.CLIENT)
    public static void loadClientModels(ModelEvent.RegisterAdditional event) {
        if (handlers.containsKey(Toolshed.MODID) && handlers.get(Toolshed.MODID) instanceof ToolshedClientHandler handler) {
            handler.addModelEvent(event);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void loadClient(FMLClientSetupEvent event) {
        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        for (String namespace : manager.getNamespaces()) {
            Optional<Resource> resource = manager.getResource(new ResourceLocation(namespace, Toolshed.MODID + ".json"));
            if (resource.isPresent()) {
                try {
                    JsonReader reader = new JsonReader(resource.get().openAsReader());
                    JsonParser parser = new JsonParser();
                    JsonObject json = parser.parse(reader).getAsJsonObject();
                    for (String handler : handlers.keySet()) {
                        if (json.has(handler)) {
                            handlers.get(handler).fromJson(json.getAsJsonObject(handler));
                        }
                    }
                } catch (IOException e) {
                    Toolshed.LOGGER.error("Malformed toolshed.json in {}", namespace);
                }
            }
        }
        for (ResourceFileHandler handler : handlers.values()) {
            handler.clientSetupEvent(event);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static JsonObject loadJson(ResourceLocation location) {
        return Data.readJson(location, Minecraft.getInstance().getResourceManager());
    }
}
