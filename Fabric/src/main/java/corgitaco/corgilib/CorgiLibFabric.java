package corgitaco.corgilib;

import corgitaco.corgilib.network.FabricNetworkHandler;
import corgitaco.corgilib.server.commands.CorgiLibCommands;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class CorgiLibFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CorgiLib.init();
        FabricNetworkHandler.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, commandBuildContext, commandSelection) -> CorgiLibCommands.register(dispatcher, commandSelection));
    }
}
