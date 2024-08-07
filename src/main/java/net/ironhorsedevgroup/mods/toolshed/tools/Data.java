package net.ironhorsedevgroup.mods.toolshed.tools;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;

public class Data {
    public static JsonObject readJson(ResourceLocation location, ResourceManager manager) {
        try {
            if (manager.getResource(location).isPresent()) {
                JsonReader reader = new JsonReader(manager.getResource(location).get().openAsReader());
                JsonParser parser = new JsonParser();
                return parser.parse(reader).getAsJsonObject();
            }
        } catch (Exception ignored) {}
        return new JsonObject();
    }

    public static JsonObject readAssets(ResourceLocation location) {
        return readJson(location, Minecraft.getInstance().getResourceManager());
    }

    public static JsonObject readData(ResourceLocation location, MinecraftServer server) {
        return readJson(location, server.getResourceManager());
    }
}
