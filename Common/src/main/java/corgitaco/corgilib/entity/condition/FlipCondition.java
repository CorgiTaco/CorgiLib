package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class FlipCondition implements Condition {

    public static final Codec<FlipCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(Condition.CODEC.fieldOf("condition_to_flip").forGetter(flipCondition -> flipCondition.condition)).apply(builder, FlipCondition::new));

    private final Condition condition;

    public FlipCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public boolean passes(ConditionContext conditionContext) {
        return !this.condition.passes(conditionContext);
    }

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }
}