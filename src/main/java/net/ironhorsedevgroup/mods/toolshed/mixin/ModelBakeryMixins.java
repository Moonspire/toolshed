package net.ironhorsedevgroup.mods.toolshed.mixin;

import net.ironhorsedevgroup.mods.toolshed.Toolshed;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBakery.class)
public class ModelBakeryMixins {

    @Inject(at = @At("HEAD"), method = "loadModel(Lnet/minecraft/resources/ResourceLocation;)V")
    private void loadModel(ResourceLocation location, CallbackInfo callback) {
        if (location.getNamespace().equals("gunsmoke")) {
            Toolshed.LOGGER.info("Loading model: {}", location);
        }
    }
}
