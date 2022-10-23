package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;

public class IsSwimmingCondition implements Condition {

    public static final Codec<IsSwimmingCondition> CODEC = Codec.unit(IsSwimmingCondition::new);

    @Override
    public boolean passes(ConditionContext conditionContext) {
        return conditionContext.entity().isSwimming();
    }

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }
}