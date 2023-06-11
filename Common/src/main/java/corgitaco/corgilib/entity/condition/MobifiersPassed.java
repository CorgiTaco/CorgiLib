package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.corgilib.comparator.DoubleComparator;

public class MobifiersPassed implements Condition {

    public static final Codec<MobifiersPassed> CODEC = RecordCodecBuilder.create(builder -> builder.group(DoubleComparator.CODEC.fieldOf("mobifiers_passed_comparator").forGetter(mobifiersPassed -> mobifiersPassed.doubleComparator)
    ).apply(builder, MobifiersPassed::new));

    private final DoubleComparator doubleComparator;

    public MobifiersPassed(DoubleComparator doubleComparator) {
        this.doubleComparator = doubleComparator;
    }

    @Override
    public boolean passes(ConditionContext conditionContext) {
        return doubleComparator.check(conditionContext.previousConditionsPassed());
    }

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }
}