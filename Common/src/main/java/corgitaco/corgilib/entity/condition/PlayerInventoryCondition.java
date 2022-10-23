package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.corgilib.entity.ItemStackCheck;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;

public class PlayerInventoryCondition implements Condition {

    public static final Codec<PlayerInventoryCondition> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(ItemStackCheck.CODEC.listOf().fieldOf("has").forGetter(wearingCondition -> wearingCondition.stackChecks)).apply(builder, PlayerInventoryCondition::new);
    });

    private final List<ItemStackCheck> stackChecks;
    private final Map<Item, ItemStackCheck> itemItemStackCheckMap;

    public PlayerInventoryCondition(List<ItemStackCheck> stackChecks) {
        if (stackChecks.isEmpty()) {
            throw new IllegalArgumentException("No item stack checks were specified.");
        }
        this.stackChecks = stackChecks;
        this.itemItemStackCheckMap = new Object2ObjectOpenHashMap<>();
        for (ItemStackCheck stackCheck : stackChecks) {
            final Item item = stackCheck.getItem();
            if (itemItemStackCheckMap.containsKey(item)) {
                throw new UnsupportedOperationException("Found another check for an already existing item.");
            } else {
                itemItemStackCheckMap.put(item.asItem(), stackCheck);
            }
        }
    }

    @Override
    public boolean passes(ConditionContext conditionContext) {
        if (conditionContext.entity() instanceof Player) {
            for (ItemStack item : ((Player) conditionContext.entity()).getInventory().items) {
                if (itemItemStackCheckMap.containsKey(item.getItem())) {
                    ItemStackCheck itemStackCheck = itemItemStackCheckMap.get(item.getItem());
                    if (!itemStackCheck.test(item)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }
}
