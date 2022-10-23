package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record ConditionsPassCondition(List<Condition> filters) implements Condition {

    public static final Codec<ConditionsPassCondition> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Condition.CODEC.listOf().fieldOf("filters").forGetter(ConditionsPassCondition::filters)
            ).apply(builder, ConditionsPassCondition::new)
    );

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }

    @Override
    public boolean passes(ConditionContext conditionContext) {
        for (Condition filter : filters) {
            if (!filter.passes(conditionContext)) {
                return false;
            }
        }
        return true;
    }
}
