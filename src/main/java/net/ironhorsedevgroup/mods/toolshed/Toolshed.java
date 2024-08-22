package net.ironhorsedevgroup.mods.toolshed;

import com.mojang.logging.LogUtils;
import net.ironhorsedevgroup.mods.toolshed.interfaces.ItemRightClickTrigger;
import net.ironhorsedevgroup.mods.toolshed.tools.Color;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Toolshed.MODID)
public class Toolshed {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "toolshed";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public Toolshed() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber
    public static class ModEvents {
        private static final int USEDURATION = 20;

        @SubscribeEvent
        public static void OnEntityInteract(PlayerInteractEvent.EntityInteract event) {
            // Interprets interaction with entities to call "public int mobInteract(ItemStack itemstack, Entity entity, Player player)" in the item used in interaction.
            Player player = event.getEntity();
            Entity target = event.getTarget();
            InteractionHand interactionhand = event.getHand();
            ItemStack itemstack = player.getItemInHand(interactionhand);
            Item item = itemstack.getItem();
            if (!player.getCooldowns().isOnCooldown(item)) {
                int itemCooldownModifier = 0;
                if (item instanceof ItemRightClickTrigger itemrct) {
                    itemCooldownModifier = itemrct.mobInteract(interactionhand, target, player);
                    if (itemstack.getUseDuration() + itemCooldownModifier > USEDURATION) {
                        player.getCooldowns().addCooldown(item, itemstack.getUseDuration() + itemCooldownModifier);
                    } else {
                        player.getCooldowns().addCooldown(item, USEDURATION);
                    }
                }
            }
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }
    }
}
