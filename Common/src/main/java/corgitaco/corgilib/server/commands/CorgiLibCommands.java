package corgitaco.corgilib.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;

public class CorgiLibCommands {

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext) {
        LiteralArgumentBuilder<CommandSourceStack> root = LiteralArgumentBuilder.literal("corgilib");

        PlaceAllCommand.register(root, commandBuildContext);

        dispatcher.register(root);
    }
}