package net.ironhorsedevgroup.mods.toolshed.content_packs.data;

import com.google.gson.JsonObject;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.server.ServerStartingEvent;

public interface DataFileHandler {
    void fromJson(JsonObject json);
    void serverSetupEvent(ServerStartingEvent event);
    default void joinSTC(ServerPlayer player) { }
}
