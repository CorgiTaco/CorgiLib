package corgitaco.corgilib.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import corgitaco.corgilib.CorgiLib;
import corgitaco.corgilib.entity.condition.Condition;
import corgitaco.corgilib.entity.npc.VillagerTradeRegistry;
import corgitaco.corgilib.math.blendingfunction.BlendingFunction;
import corgitaco.corgilib.mixin.access.RegistryAccessor;
import corgitaco.corgilib.world.level.feature.CorgiLibFeatures;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerTrades;

public class CorgiLibRegistry {

    public static final ResourceKey<Registry<Codec<? extends BlendingFunction>>> BLENDING_FUNCTION_RESOURCE_KEY = ResourceKey.createRegistryKey(CorgiLib.createLocation("blending_function"));

    public static final Registry<Codec<? extends BlendingFunction>> BLENDING_FUNCTION = RegistryAccessor.cl_invokeRegisterSimple(BLENDING_FUNCTION_RESOURCE_KEY, Lifecycle.stable(), registry -> BlendingFunction.CODEC);

    public static final ResourceKey<Registry<Codec<? extends VillagerTrades.ItemListing>>> VILLAGER_TRADES_ITEM_LISTING_RESOURCE_KEY = ResourceKey.createRegistryKey(CorgiLib.createLocation("villager_trades_item_listing"));

    public static final Registry<Codec<? extends VillagerTrades.ItemListing>> VILLAGER_TRADES_ITEM_LISTING = RegistryAccessor.cl_invokeRegisterSimple(VILLAGER_TRADES_ITEM_LISTING_RESOURCE_KEY, Lifecycle.stable(), registry -> VillagerTradeRegistry.ITEM_LISTING_CODEC);

    public static final ResourceKey<Registry<Codec<? extends Condition>>> CONDITION_KEY = ResourceKey.createRegistryKey(new ResourceLocation(CorgiLib.MOD_ID, "condition"));

    public static final Registry<Codec<? extends Condition>> CONDITION = RegistryAccessor.cl_invokeRegisterSimple(CONDITION_KEY, Lifecycle.stable(), registry -> Condition.CODEC);

    public static void init() {
        BlendingFunction.register();
        VillagerTradeRegistry.register();
        CorgiLibFeatures.register();
        Condition.register();
    }
}
