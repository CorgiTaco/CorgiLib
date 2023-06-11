package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;

import java.util.ArrayList;
import java.util.Collection;

public class EveryAmountOfDaysCondition implements Condition {

    public static final Codec<EveryAmountOfDaysCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(Codec.INT.listOf().fieldOf("amount_of_days").forGetter(everyAmountOfDaysCondition -> new ArrayList<>(everyAmountOfDaysCondition.amountOfDays)),
            Codec.LONG.optionalFieldOf("day_length", 24000L).forGetter(everyAmountOfDaysCondition -> everyAmountOfDaysCondition.dayLength),
            Codec.INT.optionalFieldOf("day_offset", 0).forGetter(everyAmountOfDaysCondition -> everyAmountOfDaysCondition.offset)
    ).apply(builder, EveryAmountOfDaysCondition::new));

    private final IntSet amountOfDays = new IntArraySet();
    private final int offset;
    private final long dayLength;

    public EveryAmountOfDaysCondition(Collection<Integer> amountOfDays, long dayLength, int offset) {
        this.offset = offset;
        this.dayLength = dayLength;
        if (amountOfDays.isEmpty()) {
            throw new IllegalArgumentException("No amount of day were specified.");
        }

        this.amountOfDays.addAll(amountOfDays);
    }


    @Override
    public boolean passes(ConditionContext conditionContext) {
        for (int amountOfDays : this.amountOfDays) {
            long worldDay = (conditionContext.world().getDayTime() / dayLength) + offset;
            if (worldDay / amountOfDays == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }
}
