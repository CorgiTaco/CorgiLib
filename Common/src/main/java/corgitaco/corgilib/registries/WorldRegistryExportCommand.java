package corgitaco.corgilib.registries;

import blue.endless.jankson.JsonElement;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.DynamicOps;
import corgitaco.corgilib.platform.services.ModPlatform;
import corgitaco.corgilib.serialization.jankson.JanksonJsonOps;
import corgitaco.corgilib.serialization.jankson.JanksonUtil;
import it.unimi.dsi.fastutil.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WorldRegistryExportCommand {

    public static final Map<String, String> COMMENTS = Util.make(new HashMap<>(), map -> {
    });


    public static LiteralArgumentBuilder<CommandSourceStack> registerWorldRegistryExportCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        return registerWorldRegistryExportCommand(dispatcher, "worldRegistryExport");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> registerWorldRegistryExportCommand(CommandDispatcher<CommandSourceStack> dispatcher, String commandString) {

        Function<CommandContext<CommandSourceStack>, Boolean> withComments = cs -> ((CommandContext<CommandSourceStack>) cs).getArgument("With comments?", Boolean.class);
        Function<CommandContext<CommandSourceStack>, Boolean> builtin = cs -> ((CommandContext<CommandSourceStack>) cs).getArgument("Generate Built In Registries?", Boolean.class);

        return Commands.literal(commandString).requires(stack -> stack.hasPermission(4))
                .executes(cs -> generateWorldRegistryExport(true, false, cs))
                .then(Commands.argument("With comments?", BoolArgumentType.bool()).executes(cs -> WorldRegistryExportCommand.generateWorldRegistryExport(withComments.apply(cs), false, cs))

                        .then(Commands.argument("Generate Built In Registries?", BoolArgumentType.bool())
                                .executes(cs -> WorldRegistryExportCommand.generateWorldRegistryExport(withComments.apply(cs), builtin.apply(cs), cs))));
    }

    public static int generateWorldRegistryExport(boolean withComments, boolean builtin, CommandContext<CommandSourceStack> commandSource) {
        CommandSourceStack source = commandSource.getSource();
        if (!source.getServer().isSingleplayer() || source.getServer().getPlayerCount() > 1) {
            source.sendFailure(Component.translatable("corgilib.worldRegistryExport.singleplayer").withStyle(ChatFormatting.RED));
            return 0;
        }


        Path finalExportPath = ModPlatform.PLATFORM.modConfigDir().resolve("world_registry_export").resolve(builtin ? "builtin" : "world").resolve("data");
        Component exportFileComponent = Component.literal(finalExportPath.toString()).withStyle(ChatFormatting.UNDERLINE).withStyle(text -> text.withColor(TextColor.fromLegacyFormat(ChatFormatting.AQUA)).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, finalExportPath.toString())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("corgilib.clickevent.hovertext"))));

        if (finalExportPath.toFile().exists()) {
            source.sendFailure(Component.translatable("corgilib.worldRegistryExport.exists", exportFileComponent).withStyle(ChatFormatting.RED));
            return 0;
        }
        source.sendSuccess(Component.translatable("corgilib.worldRegistryExport.starting").withStyle(ChatFormatting.YELLOW), true);

        try {
            generateFiles(builtin, source, finalExportPath, withComments);

            createPackMCMeta(finalExportPath.getParent(), builtin);
            source.sendSuccess(Component.translatable("corgilib.worldRegistryExport.success", exportFileComponent).withStyle(ChatFormatting.GREEN), true);
            return 1;
        } catch (IOException e) {
            source.sendFailure(Component.translatable("corgilib.worldRegistryExport.failed"));
            e.printStackTrace();
            return 0;
        }
    }

    private static void generateFiles(boolean builtin, CommandSourceStack source, Path exportPath, boolean withComments) throws IOException {
        Files.createDirectories(exportPath);
        RegistryAccess registry = builtin ? RegistryAccess.builtinCopy() : source.getLevel().registryAccess();

        DynamicOps<JsonElement> ops = RegistryOps.create(JanksonJsonOps.INSTANCE, registry);

        for (RegistryAccess.RegistryData<?> knownRegistry : RegistryAccess.knownRegistries()) {
            dumpRegistryCap(exportPath, registry, ops, knownRegistry, withComments);
        }
    }


    private static <T> void dumpRegistryCap(Path root, RegistryAccess registryAccess, DynamicOps<JsonElement> ops, RegistryAccess.RegistryData<T> data, boolean withComments) {
        ResourceKey<? extends Registry<T>> resourceKey = data.key();
        Registry<T> registry = registryAccess.ownedRegistryOrThrow(resourceKey);

        for (Map.Entry<ResourceKey<T>, T> resourceKeyTEntry : registry.entrySet()) {
            ResourceKey<T> resourceKeyTEntryKey = resourceKeyTEntry.getKey();

            Path resolve = root.resolve(resourceKeyTEntryKey.location().getNamespace());

            String resourceKeyNamespace = resourceKey.location().getNamespace();
            if (ModPlatform.PLATFORM.getPlatformName().equalsIgnoreCase("Forge") && !resourceKeyNamespace.equalsIgnoreCase("minecraft")) {
                resolve = resolve.resolve(resourceKeyNamespace);
            }

            Path path = resolve.resolve(resourceKey.location().getPath()).resolve(resourceKeyTEntryKey.location().getPath() + ".json");
            try {
                Optional<JsonElement> jsonElement = data.codec().encodeStart(ops, resourceKeyTEntry.getValue()).result();

                if (jsonElement.isPresent()) {
                    Files.createDirectories(path.getParent());

                    JsonElement loadedElement = jsonElement.get();
                    if (withComments) {
                        loadedElement = JanksonUtil.addCommentsAndAlphabeticallySortRecursively(COMMENTS, loadedElement, "", true);
                        Files.write(path, loadedElement.toJson(JanksonUtil.JSON_GRAMMAR).getBytes());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private static void createPackMCMeta(Path path, boolean builtIn) throws IOException {
        String fileString = "{\n" +
                "\t\"pack\":{\n" +
                "\t\t\"pack_format\": " + SharedConstants.DATA_PACK_FORMAT + ",\n" +
                "\t\t\"description\": \"" + " Generated world gen datapack from " + (builtIn ? "built in registries" : "current world registries") + ".\"\n" +
                "\t}\n" +
                "}\n";

        Files.write(path.resolve("pack.mcmeta"), fileString.getBytes());
    }
}
