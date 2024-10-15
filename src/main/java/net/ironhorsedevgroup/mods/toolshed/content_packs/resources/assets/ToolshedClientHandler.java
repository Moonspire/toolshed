package net.ironhorsedevgroup.mods.toolshed.content_packs.resources.assets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.ArrayList;
import java.util.List;

public class ToolshedClientHandler implements ResourceFileHandler {
    private List<ModelResourceLocation> forceLoadModels = new ArrayList<>();

    public ToolshedClientHandler() {}

    @Override
    public void fromJson(JsonObject json) {
        if (json.has("models")) {
            for (JsonElement entry : json.getAsJsonArray("models")) {
                forceLoadModels.add(new ModelResourceLocation(entry.getAsString(), "inventory"));
            }
        }
    }

    @Override
    public void clientSetupEvent(FMLClientSetupEvent event) {

    }

    public void addModelEvent(ModelEvent.RegisterAdditional event) {
        for (ModelResourceLocation model : forceLoadModels) {
            event.register(model);
        }
        forceLoadModels = new ArrayList<>();
    }
}
