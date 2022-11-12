package corgitaco.corgilib.platform;

import com.google.auto.service.AutoService;
import corgitaco.corgilib.network.FabricNetworkHandler;
import corgitaco.corgilib.network.Packet;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerPlayer;

import java.nio.file.Path;

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

    @Override
    public Path configDir() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
