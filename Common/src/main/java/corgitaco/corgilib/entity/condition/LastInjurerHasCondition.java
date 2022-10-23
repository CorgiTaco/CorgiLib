package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class LastInjurerHasCondition implements Condition {
    public static Codec<LastInjurerHasCondition> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(Condition.CODEC.listOf().fieldOf("conditions_to_apply").forGetter(lastInjurerByTypeHasCondition -> lastInjurerByTypeHasCondition.injurerConditions)
        ).apply(builder, LastInjurerHasCondition::new);
    });
    private final List<Condition> injurerConditions;

    public LastInjurerHasCondition(List<Condition> injurerConditions) {
        this.injurerConditions = injurerConditions;
    }

    @Override
    public boolean passes(ConditionContext conditionContext) {
        LivingEntity lastHurtByMob = conditionContext.entity().getLastHurtByMob();

        if (lastHurtByMob == null) {
            return false;
        }

        for (Condition condition : injurerConditions) {
            if (!condition.passes(new ConditionContext(conditionContext, lastHurtByMob))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }
}
