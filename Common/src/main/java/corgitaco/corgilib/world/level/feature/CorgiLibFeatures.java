package corgitaco.corgilib.world.level.feature;


import corgitaco.corgilib.CorgiLib;
import corgitaco.corgilib.reg.RegistrationProvider;
import corgitaco.corgilib.reg.RegistryObject;
import corgitaco.corgilib.world.level.feature.gen.TreeFromStructureNBTFeature;
import corgitaco.corgilib.world.level.feature.gen.configurations.TreeFromStructureNBTConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.function.Supplier;

public class CorgiLibFeatures {

    private static final RegistrationProvider<Feature<?>> PROVIDER = RegistrationProvider.get(Registries.FEATURE, CorgiLib.MOD_ID);

    public static final RegistryObject<Feature<TreeFromStructureNBTConfig>> TREE_FROM_NBT = createFeature("tree_from_nbt", () -> new TreeFromStructureNBTFeature(TreeFromStructureNBTConfig.CODEC.stable()));


    private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryObject<F> createFeature(String id, Supplier<? extends F> feature) {
        return PROVIDER.register(id, feature);
    }

    public static void register() {
    }

}
