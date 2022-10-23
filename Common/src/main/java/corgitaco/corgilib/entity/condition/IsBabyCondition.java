package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;

public class IsBabyCondition implements Condition {

    public static final Codec<IsBabyCondition> CODEC = Codec.unit(IsBabyCondition::new);

    @Override
    public boolean passes(ConditionContext conditionContext) {
        return conditionContext.entity().isBaby();
    }

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }
}