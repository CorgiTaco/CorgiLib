package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.corgilib.math.LongPair;

import java.util.List;

public class TimeOfDayCondition implements Condition {

    public static final Codec<TimeOfDayCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(LongPair.createLongPairCodec("min_time", "max_time").listOf().fieldOf("times_of_day").forGetter(timeOfDayCondition -> timeOfDayCondition.timesOfDay),
            Codec.LONG.optionalFieldOf("day_length", 24000L).forGetter(timeOfDayCondition -> timeOfDayCondition.dayLength)
    ).apply(builder, TimeOfDayCondition::new));

    private final List<LongPair> timesOfDay;
    private final long dayLength;

    public TimeOfDayCondition(List<LongPair> timesOfDay, long dayLength) {
        this.dayLength = dayLength;
        if (timesOfDay.isEmpty()) {
            throw new IllegalArgumentException("No times of day were specified.");
        }
        this.timesOfDay = timesOfDay;
        for (LongPair longPair : this.timesOfDay) {
            if (longPair.getVal1() > this.dayLength || longPair.getVal1() < 0 || longPair.getVal2() > this.dayLength || longPair.getVal2() < 0 || longPair.getVal1() > longPair.getVal2()) {
                throw new IllegalArgumentException("minTime & maxTime must be between 0 & " + dayLength + " and minTime cannot be greater than maxTime! Pair: \"" + longPair + "\" failed.");
            }
        }
    }

    @Override
    public boolean passes(ConditionContext conditionContext) {
        for (LongPair longPair : this.timesOfDay) {
            if (longPair.isInBetween(conditionContext.world().getDayTime() % this.dayLength)) {
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
