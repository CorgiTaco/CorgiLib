package corgitaco.corgilib.entity.npc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.corgilib.CorgiLib;
import corgitaco.corgilib.core.CorgiLibRegistry;
import corgitaco.corgilib.mixin.access.villagertrades.*;
import corgitaco.corgilib.reg.RegistrationProvider;
import corgitaco.corgilib.serialization.codec.CodecUtil;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.saveddata.maps.MapDecoration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class VillagerTradeRegistry {

    public static final Codec<Item> ITEM_CODEC = CodecUtil.createLoggedExceptionRegistryCodec(Registry.ITEM);
    public static final Codec<MobEffect> MOB_EFFECT_CODEC = CodecUtil.createLoggedExceptionRegistryCodec(Registry.MOB_EFFECT);

    private final static Codec<VillagerTrades.EmeraldForItems> EMERALD_FOR_ITEMS_CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    ITEM_CODEC.fieldOf("item").forGetter(listing -> ((EmeraldForItemsAccess) listing).byg_getItem()),
                    Codec.INT.fieldOf("cost").forGetter(listing -> ((EmeraldForItemsAccess) listing).byg_getCost()),
                    Codec.INT.fieldOf("max_uses").forGetter(listing -> ((EmeraldForItemsAccess) listing).byg_getMaxUses()),
                    Codec.INT.fieldOf("villager_xp").forGetter(listing -> ((EmeraldForItemsAccess) listing).byg_getVillagerXp())
            ).apply(builder, VillagerTrades.EmeraldForItems::new)
    );

    private final static Codec<VillagerTrades.ItemsForEmeralds> ITEMS_FOR_EMERALDS_CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    ITEM_CODEC.fieldOf("item").forGetter(listing -> ((ItemsForEmeraldsAccess) listing).byg_getItemStack().getItem()),
                    Codec.INT.fieldOf("emerald_cost").forGetter(listing -> ((ItemsForEmeraldsAccess) listing).byg_getEmeraldCost()),
                    Codec.INT.fieldOf("number_of_items").forGetter(listing -> ((ItemsForEmeraldsAccess) listing).byg_getNumberOfItems()),
                    Codec.INT.fieldOf("max_uses").forGetter(listing -> ((ItemsForEmeraldsAccess) listing).byg_getMaxUses()),
                    Codec.INT.fieldOf("villager_xp").forGetter(listing -> ((ItemsForEmeraldsAccess) listing).byg_getVillagerXp())
            ).apply(builder, VillagerTrades.ItemsForEmeralds::new)
    );

    private final static Codec<VillagerTrades.ItemsAndEmeraldsToItems> ITEMS_AND_EMERALDS_TO_ITEMS_CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    ITEM_CODEC.fieldOf("from_item").forGetter(listing -> ((ItemsAndEmeraldsForItemsAccess) listing).byg_getFromItem().getItem()),
                    Codec.INT.fieldOf("from_count").forGetter(listing -> ((ItemsAndEmeraldsForItemsAccess) listing).byg_getFromCount()),
                    Codec.INT.fieldOf("emerald_cost").forGetter(listing -> ((ItemsAndEmeraldsForItemsAccess) listing).byg_getEmeraldCost()),
                    ITEM_CODEC.fieldOf("to_item").forGetter(listing -> ((ItemsAndEmeraldsForItemsAccess) listing).byg_getToItem().getItem()),
                    Codec.INT.fieldOf("to_count").forGetter(listing -> ((ItemsAndEmeraldsForItemsAccess) listing).byg_getToCount()),
                    Codec.INT.fieldOf("max_uses").forGetter(listing -> ((ItemsAndEmeraldsForItemsAccess) listing).byg_getMaxUses()),
                    Codec.INT.fieldOf("villager_xp").forGetter(listing -> ((ItemsAndEmeraldsForItemsAccess) listing).byg_getVillagerXp())
            ).apply(builder, VillagerTrades.ItemsAndEmeraldsToItems::new)
    );

    private final static Codec<VillagerTrades.SuspiciousStewForEmerald> SUSPICIOUS_STEW_FOR_EMERALD_CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    MOB_EFFECT_CODEC.fieldOf("mob_effect").forGetter(listing -> ((SuspiciousStewForEmeraldAccess) listing).byg_getEffect()),
                    Codec.INT.fieldOf("duration").forGetter(listing -> ((SuspiciousStewForEmeraldAccess) listing).byg_getDuration()),
                    Codec.INT.fieldOf("xp").forGetter(listing -> ((SuspiciousStewForEmeraldAccess) listing).byg_getXp())
            ).apply(builder, VillagerTrades.SuspiciousStewForEmerald::new)
    );

    public static final Codec<MapDecoration.Type> MAP_DECORATION_TYPE_CODEC = Codec.STRING.comapFlatMap(type -> {
        try {
            return DataResult.success(MapDecoration.Type.valueOf(type.toUpperCase()));
        } catch (Exception e) {
            return DataResult.error("Invalid Map Decoration Type! You put \"%s\". Valid values: %s".formatted(type, Arrays.toString(MapDecoration.Type.values())));
        }
    }, MapDecoration.Type::name);

    private final static Codec<VillagerTrades.TreasureMapForEmeralds> TREASURE_MAP_FOR_EMERALDS_CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.INT.fieldOf("emerald_cost").forGetter(listing -> ((TreasureMapForEmeraldAccess) listing).byg_getEmeraldCost()),
                    TagKey.hashedCodec(Registry.STRUCTURE_REGISTRY).fieldOf("destination").forGetter(listing -> ((TreasureMapForEmeraldAccess) listing).byg_getDestination()),
                    Codec.STRING.fieldOf("display_name").forGetter(listing -> ((TreasureMapForEmeraldAccess) listing).byg_getDisplayName()),
                    MAP_DECORATION_TYPE_CODEC.fieldOf("destination_type").forGetter(listing -> ((TreasureMapForEmeraldAccess) listing).byg_getDestinationType()),
                    Codec.INT.fieldOf("max_uses").forGetter(listing -> ((TreasureMapForEmeraldAccess) listing).byg_getMaxUses()),
                    Codec.INT.fieldOf("villager_xp").forGetter(listing -> ((TreasureMapForEmeraldAccess) listing).byg_getVillagerXp())
            ).apply(builder, VillagerTrades.TreasureMapForEmeralds::new)
    );

    private static final Map<Class<? extends VillagerTrades.ItemListing>, Codec<? extends VillagerTrades.ItemListing>> ITEM_LISTING_CLASS_BY_CODEC = Util.make(new HashMap<>(), map -> {
        map.put(VillagerTrades.EmeraldForItems.class, EMERALD_FOR_ITEMS_CODEC);
        map.put(VillagerTrades.ItemsForEmeralds.class, ITEMS_FOR_EMERALDS_CODEC);
        map.put(VillagerTrades.SuspiciousStewForEmerald.class, SUSPICIOUS_STEW_FOR_EMERALD_CODEC);
        map.put(VillagerTrades.ItemsAndEmeraldsToItems.class, ITEMS_AND_EMERALDS_TO_ITEMS_CODEC);
        map.put(VillagerTrades.TreasureMapForEmeralds.class, TREASURE_MAP_FOR_EMERALDS_CODEC);
    });

    public static final Codec<VillagerTrades.ItemListing> ITEM_LISTING_CODEC = CorgiLibRegistry.VILLAGER_TRADES_ITEM_LISTING.byNameCodec()
            .dispatchStable(itemListing -> ITEM_LISTING_CLASS_BY_CODEC.get(itemListing.getClass()), codec -> codec.fieldOf("config").codec());

    public static void register() {
        final var provider = RegistrationProvider.get(CorgiLibRegistry.VILLAGER_TRADES_ITEM_LISTING_RESOURCE_KEY, CorgiLib.MOD_ID);

        provider.register("emerald_for_items", () -> EMERALD_FOR_ITEMS_CODEC);
        provider.register("items_for_emeralds", () -> ITEMS_FOR_EMERALDS_CODEC);
        provider.register("suspicious_stew_for_emerald", () -> SUSPICIOUS_STEW_FOR_EMERALD_CODEC);
        provider.register("items_and_emeralds_to_items", () -> ITEMS_AND_EMERALDS_TO_ITEMS_CODEC);
        provider.register("treasure_map_for_emeralds", () -> TREASURE_MAP_FOR_EMERALDS_CODEC);
    }
}
