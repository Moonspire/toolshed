package net.ironhorsedevgroup.mods.toolshed.tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;

public class Data {
    public static JsonObject readJson(ResourceLocation location, ResourceManager manager) {
        location = new ResourceLocation(location.getNamespace(), location.getPath() + ".json");
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
        location = new ResourceLocation(location.getNamespace(), location.getPath() + ".json");
        return readJson(location, Minecraft.getInstance().getResourceManager());
    }

    public static JsonObject readData(ResourceLocation location, MinecraftServer server) {
        location = new ResourceLocation(location.getNamespace(), location.getPath() + ".json");
        return readJson(location, server.getResourceManager());
    }

    public static class DataObject {
        private final JsonObject json;

        private DataObject() {
            json = new JsonObject();
        }

        public DataObject(JsonObject json) {
            this.json = json;
        }

        public DataObject getObject(String target) {
            if (json.has(target)) {
                return new DataObject(json.getAsJsonObject(target));
            }
            return new DataObject();
        }

        public JsonArray getArray(String target) {
            if (json.has(target)) {
                return json.getAsJsonArray(target);
            }
            return new JsonArray();
        }

        public JsonElement get() {
            return json;
        }
    }
}
