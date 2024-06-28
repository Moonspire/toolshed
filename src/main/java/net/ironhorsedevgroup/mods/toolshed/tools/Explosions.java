package net.ironhorsedevgroup.mods.toolshed.tools;

import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public class Explosions {
    public static void explode(Level level, Double force, Position position, Entity entity) {
        explode(level, force, position, entity, Explosion.BlockInteraction.BREAK);
    }

    public static void explode(Level level, Double force, Position position, Entity entity, Explosion.BlockInteraction interaction) {
        level.explode(entity, position.x(), position.y(), position.z(), force.floatValue(), interaction);
    }
}
