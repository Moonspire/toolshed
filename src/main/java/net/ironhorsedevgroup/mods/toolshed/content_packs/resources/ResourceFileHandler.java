package net.ironhorsedevgroup.mods.toolshed.content_packs.resources;

import com.google.gson.JsonObject;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public interface ResourceFileHandler {
    void fromJson(JsonObject object);
    void clientSetupEvent(FMLClientSetupEvent event);
    void addModelEvent(ModelEvent.RegisterAdditional event);
}
