package net.ironhorsedevgroup.mods.toolshed.content_packs.resources;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.ironhorsedevgroup.mods.toolshed.Toolshed;
import net.ironhorsedevgroup.mods.toolshed.tools.Data;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ResourceLoader {
    private static final Map<String, ResourceFileHandler> handlers = new HashMap<>();

    public static void addPackAssetFile(String fileName, ResourceFileHandler handler) {
        handlers.put(fileName, handler);
    }

    @OnlyIn(Dist.CLIENT)
    public static void loadClientModels(ModelEvent.RegisterAdditional event) {
        for (ResourceFileHandler handler : handlers.values()) {
            handler.addModelEvent(event);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void loadClient(FMLClientSetupEvent event) {
        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        for (String namespace : manager.getNamespaces()) {
            for (String handler : handlers.keySet()) {
                Optional<Resource> resource = manager.getResource(new ResourceLocation(namespace, handler + ".json"));
                if (resource.isPresent()) {
                    try {
                        JsonReader reader = new JsonReader(resource.get().openAsReader());
                        JsonParser parser = new JsonParser();
                        handlers.get(handler).fromJson(parser.parse(reader).getAsJsonObject());
                    } catch (Exception e) {
                        Toolshed.LOGGER.error("Malformed JSON: {}:{}", namespace, handler);
                    }
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
