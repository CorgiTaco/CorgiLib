package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;

public class IsDeadOrDyingCondition implements Condition {
    public static final IsDeadOrDyingCondition INSTANCE = new IsDeadOrDyingCondition();
    public static final Codec<IsDeadOrDyingCondition> CODEC = Codec.unit(() -> {
        return INSTANCE;
    });

    @Override
    public boolean passes(ConditionContext conditionContext) {
        return conditionContext.isDeadOrDying();
    }

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }
}
