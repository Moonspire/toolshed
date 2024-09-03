package net.ironhorsedevgroup.mods.toolshed.content_packs.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.ironhorsedevgroup.mods.toolshed.Toolshed;
import net.ironhorsedevgroup.mods.toolshed.tools.Data;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DataLoader {
    private static final Map<String, DataFileHandler> handlers = new HashMap<>();

    public static void addPackDataHandler(String key, DataFileHandler handler) {
        handlers.put(key, handler);
    }

    public static void loadServer(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        ResourceManager manager = server.getResourceManager();
        for (String namespace : manager.getNamespaces()) {
            Optional<Resource> resource = manager.getResource(new ResourceLocation(namespace, Toolshed.MODID + ".json"));
            if (resource.isPresent()) {
                try {
                    JsonReader reader = new JsonReader(resource.get().openAsReader());
                    JsonParser parser = new JsonParser();
                    JsonObject json = parser.parse(reader).getAsJsonObject();
                    for (String handler : handlers.keySet()) {
                        if (json.has(handler)) {
                            handlers.get(handler).fromJson(json.getAsJsonObject("handler"));
                        }
                    }
                } catch (IOException e) {
                    Toolshed.LOGGER.error("Malformed toolshed.json in {}", namespace);
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

    public static void playerJoinEvent(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = event.getEntity().getServer().getPlayerList().getPlayer(event.getEntity().getUUID());

        for (DataFileHandler handler : handlers.values()) {
            handler.joinSTC(player);
        }
    }
}
