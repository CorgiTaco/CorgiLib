package corgitaco.corgilib.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import corgitaco.corgilib.CorgiLib;
import corgitaco.corgilib.registries.WorldRegistryExportCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class CorgiLibCommands {

    public static void register(final CommandDispatcher<CommandSourceStack> dispatcher, final Commands.CommandSelection environmentType) {
        LiteralArgumentBuilder<CommandSourceStack> corgiLibCommands = Commands.literal(CorgiLib.MOD_ID);
        corgiLibCommands.then(WorldRegistryExportCommand.registerWorldRegistryExportCommand(dispatcher));
        dispatcher.register(corgiLibCommands);
    }
}
