package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.corgilib.entity.ItemStackCheck;
import corgitaco.corgilib.serialization.codec.CodecUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class HasEquippedCondition implements Condition {
    public static final Codec<HasEquippedCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(Codec.unboundedMap(CodecUtil.EQUIPMENT_SLOT_CODEC, ItemStackCheck.CODEC.listOf()).fieldOf("has_equipped").forGetter(hasEquippedCondition -> hasEquippedCondition.stackChecks)).apply(builder, HasEquippedCondition::new));

    private final Map<EquipmentSlot, List<ItemStackCheck>> stackChecks;
    private final Set<Map.Entry<EquipmentSlot, List<ItemStackCheck>>> stackChecksEntries;

    public HasEquippedCondition(Map<EquipmentSlot, List<ItemStackCheck>> stackChecksBySlot) {
        this.stackChecks = new Object2ObjectOpenHashMap<>(stackChecksBySlot);
        this.stackChecksEntries = this.stackChecks.entrySet();
    }

    @Override
    public boolean passes(ConditionContext conditionContext) {
        int hits = 0;
        for (Map.Entry<EquipmentSlot, List<ItemStackCheck>> stackChecksEntry : this.stackChecksEntries) {
            final ItemStack slotItemStack = conditionContext.entity().getItemBySlot(stackChecksEntry.getKey());
            final Item slotItem = slotItemStack.getItem();
            final List<ItemStackCheck> value = stackChecksEntry.getValue();
            for (ItemStackCheck itemStackCheck : value) {
                if (slotItem == itemStackCheck.getItem()) {
                    if (itemStackCheck.test(slotItemStack)) {
                        hits++;
                        break;
                    }
                }
            }
        }

        return hits == this.stackChecksEntries.size();
    }

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }
}
