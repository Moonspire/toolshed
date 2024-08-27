package net.ironhorsedevgroup.mods.toolshed.content_packs.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.ironhorsedevgroup.mods.toolshed.Toolshed;
import net.ironhorsedevgroup.mods.toolshed.tools.Data;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.event.server.ServerStartingEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DataLoader {
    private static final Map<String, DataFileHandler> handlers = new HashMap<>();

    public static void addPackDataFile(String fileName, DataFileHandler handler) {
        handlers.put(fileName, handler);
    }

    public static void loadServer(ServerStartingEvent event) {
        ResourceManager manager = event.getServer().getResourceManager();
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
        for (DataFileHandler handler : handlers.values()) {
            handler.serverSetupEvent(event);
        }
    }

    public static JsonObject loadJson(ResourceLocation location, MinecraftServer server) {
        return Data.readJson(location, server.getResourceManager());
    }
}
