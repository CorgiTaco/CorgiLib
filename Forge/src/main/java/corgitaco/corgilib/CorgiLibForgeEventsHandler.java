package corgitaco.corgilib;

import corgitaco.corgilib.server.commands.CorgiLibCommands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CorgiLibForgeEventsHandler {

    @SubscribeEvent
    public static void registerCommands(final RegisterCommandsEvent event) {
        CorgiLibCommands.register(event.getDispatcher(), event.getCommandSelection());
    }
}
