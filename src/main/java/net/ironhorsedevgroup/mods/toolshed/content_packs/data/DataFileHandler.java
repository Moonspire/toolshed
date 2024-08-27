package net.ironhorsedevgroup.mods.toolshed.content_packs.data;

import com.google.gson.JsonObject;
import net.minecraftforge.event.server.ServerStartingEvent;

public interface DataFileHandler {
    void fromJson(JsonObject object);
    void serverSetupEvent(ServerStartingEvent event);
}
