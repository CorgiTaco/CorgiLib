package com.example.examplemod.platform.services;

import com.example.examplemod.network.Packet;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public interface ModPlatform {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    <P extends Packet> void sendToClient(ServerPlayer player, P packet);

    default <P extends Packet> void sendToAllClients(List<ServerPlayer> players, P packet) {
        for (ServerPlayer player : players) {
            sendToClient(player, packet);
        }
    }

    <P extends Packet> void sendToServer(P packet);
}
