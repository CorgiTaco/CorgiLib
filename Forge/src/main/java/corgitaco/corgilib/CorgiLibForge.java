package corgitaco.corgilib;

import corgitaco.corgilib.network.ForgeNetworkHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CorgiLib.MOD_ID)
public class CorgiLibForge {

    public CorgiLibForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);
        CorgiLib.init();
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        ForgeNetworkHandler.init();
    }
}