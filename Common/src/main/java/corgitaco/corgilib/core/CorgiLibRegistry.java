package corgitaco.corgilib.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import corgitaco.corgilib.CorgiLib;
import corgitaco.corgilib.entity.condition.Condition;
import corgitaco.corgilib.entity.npc.VillagerTradeRegistry;
import corgitaco.corgilib.math.blendingfunction.BlendingFunction;
import corgitaco.corgilib.reg.RegistrationProvider;
import corgitaco.corgilib.world.level.feature.CorgiLibFeatures;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.function.Supplier;

public class CorgiLibRegistry {

    public static final ResourceKey<Registry<Codec<? extends BlendingFunction>>> BLENDING_FUNCTION_RESOURCE_KEY = ResourceKey.createRegistryKey(CorgiLib.createLocation("blending_function"));

    public static final Supplier<Registry<Codec<? extends BlendingFunction>>> BLENDING_FUNCTION = RegistrationProvider.get(BLENDING_FUNCTION_RESOURCE_KEY, CorgiLib.MOD_ID).registryBuilder().build();

    public static final ResourceKey<Registry<Codec<? extends VillagerTrades.ItemListing>>> VILLAGER_TRADES_ITEM_LISTING_RESOURCE_KEY = ResourceKey.createRegistryKey(CorgiLib.createLocation("villager_trades_item_listing"));

    public static final Supplier<Registry<Codec<? extends VillagerTrades.ItemListing>>> VILLAGER_TRADES_ITEM_LISTING = RegistrationProvider.get(VILLAGER_TRADES_ITEM_LISTING_RESOURCE_KEY, CorgiLib.MOD_ID).registryBuilder().build();

    public static final ResourceKey<Registry<Codec<? extends Condition>>> CONDITION_KEY = ResourceKey.createRegistryKey(new ResourceLocation(CorgiLib.MOD_ID, "condition"));

    public static final Supplier<Registry<Codec<? extends Condition>>> CONDITION = RegistrationProvider.get(CONDITION_KEY, CorgiLib.MOD_ID).registryBuilder().build();

    public static void init() {
        BlendingFunction.register();
        VillagerTradeRegistry.register();
        CorgiLibFeatures.register();
        Condition.register();
    }
}
