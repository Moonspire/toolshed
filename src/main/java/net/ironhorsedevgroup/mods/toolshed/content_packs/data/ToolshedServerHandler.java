package net.ironhorsedevgroup.mods.toolshed.content_packs.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.toolshed.materials.Materials;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.server.ServerStartingEvent;

import java.util.ArrayList;
import java.util.List;

public class ToolshedServerHandler implements DataFileHandler {
    private List<ResourceLocation> materials = new ArrayList<>();

    public ToolshedServerHandler() {}

    @Override
    public void fromJson(JsonObject json) {
        if (json.has("materials")) {
            for (JsonElement entry : json.getAsJsonArray("materials")) {
                materials.add(new ResourceLocation(entry.getAsString()));
            }
        }
    }

    @Override
    public void serverSetupEvent(ServerStartingEvent event) {
        Materials.loadMaterials(materials, event.getServer());
        materials = null;
    }

    @Override
    public void joinSTC(ServerPlayer player) {
        Materials.sendMaterials(player);
    }
}
