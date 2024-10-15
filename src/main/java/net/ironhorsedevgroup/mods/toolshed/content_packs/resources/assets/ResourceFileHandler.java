package net.ironhorsedevgroup.mods.toolshed.content_packs.resources.assets;

import com.google.gson.JsonObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public interface ResourceFileHandler {
    void fromJson(JsonObject json);
    void clientSetupEvent(FMLClientSetupEvent event);
}
