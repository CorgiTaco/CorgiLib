package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ChanceCondition implements Condition {

    public static final Codec<ChanceCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(Codec.DOUBLE.fieldOf("chance").forGetter(chanceCondition -> chanceCondition.chance)
    ).apply(builder, ChanceCondition::new));

    private final double chance;

    public ChanceCondition(double chance) {
        this.chance = chance;
    }

    @Override
    public boolean passes(ConditionContext conditionContext) {
        return conditionContext.isDeadOrDying() && chance > conditionContext.world().getRandom().nextDouble();
    }

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }
}