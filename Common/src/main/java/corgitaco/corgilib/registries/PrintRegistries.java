package corgitaco.corgilib.registries;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PrintRegistries {


    public static void printRegistry(RegistryAccess access, Path path) {
        StringBuilder filetxt = new StringBuilder();
        for (Registry<?> registry : Registry.REGISTRY) {
            filetxt.append(registry.key()).append("\n");
            filetxt.append(dumpRegistryElements(registry));
            filetxt.append("\n\n");
        }
        filetxt.append("\n----------------------------------Dynamic Registries----------------------------------\n\n\n");
        access.registries().forEach((registryEntry) -> {
            filetxt.append(registryEntry.key()).append("\n");
            filetxt.append(dumpRegistryElements(registryEntry.value()));
            filetxt.append("\n\n");
        });

        try {
            Files.write(path, filetxt.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static <T> StringBuilder dumpRegistryElements(Registry<T> registry) {
        StringBuilder registryElements = new StringBuilder();
        int i = 0;
        for (T t : registry) {
            registryElements.append(i++).append(". \"").append(registry.getKey(t).toString()).append("\"\n");
        }

        return registryElements;
    }
}
