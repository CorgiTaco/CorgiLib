package com.example.examplemod.network;

import com.example.examplemod.Constants;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface Packet {
    Map<String, Handler<?>> S2C_PACKETS = Util.make(new HashMap<>(), map -> {
        Constants.LOGGER.info("Initializing network...");
        Constants.LOGGER.info("Initialized network!");
    });


    void write(FriendlyByteBuf buf);

    void handle(@Nullable Level level, @Nullable Player player);

    record Handler<T extends Packet>(Class<T> clazz, PacketDirection direction, BiConsumer<T, FriendlyByteBuf> write,
                                     Function<FriendlyByteBuf, T> read,
                                     Handle<T> handle) {
    }

    enum PacketDirection {
        SERVER_TO_CLIENT,
        CLIENT_TO_SERVER
    }

    @FunctionalInterface
    interface Handle<T extends Packet> {
        void handle(T packet, Level level, Player player);
    }
}
