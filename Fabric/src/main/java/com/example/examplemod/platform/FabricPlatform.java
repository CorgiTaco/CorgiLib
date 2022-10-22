package com.example.examplemod.platform;

import com.example.examplemod.network.FabricNetworkHandler;
import com.example.examplemod.network.Packet;
import com.example.examplemod.platform.services.ModPlatform;
import com.google.auto.service.AutoService;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerPlayer;

@AutoService(ModPlatform.class)
public class FabricPlatform implements ModPlatform {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <P extends Packet> void sendToClient(ServerPlayer player, P packet) {
        FabricNetworkHandler.sendToPlayer(player, packet);
    }

    @Override
    public <P extends Packet> void sendToServer(P packet) {
        FabricNetworkHandler.sendToServer(packet);
    }
}
