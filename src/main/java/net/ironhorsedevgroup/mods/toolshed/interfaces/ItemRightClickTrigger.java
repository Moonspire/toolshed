package net.ironhorsedevgroup.mods.toolshed.interfaces;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public interface ItemRightClickTrigger {
    int mobInteract(InteractionHand hand, Entity entity, Player player);
}
