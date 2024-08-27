package net.ironhorsedevgroup.mods.toolshed.mixin;

import net.ironhorsedevgroup.mods.toolshed.content_packs.resources.model.ItemModelOverrides;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemModelShaper.class)
public class ItemModelShaperMixins {

    @Inject(at = @At("HEAD"), method = "getItemModel(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/resources/model/BakedModel;", cancellable = true)
    public void getItemModel(ItemStack stack, CallbackInfoReturnable<BakedModel> callback) {
        if (ItemModelOverrides.isOverwrote(stack.getItem())) {
            callback.setReturnValue(ItemModelOverrides.getModel(stack));
        }
    }
}
