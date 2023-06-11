package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.corgilib.serialization.codec.CodecUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Map;

public class LastInjurerByTypeHasCondition implements Condition {
    public static Codec<LastInjurerByTypeHasCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(Codec.unboundedMap(CodecUtil.ENTITY_TYPE_CODEC, Condition.CODEC.listOf()).fieldOf("conditions_to_apply").forGetter(lastInjurerByTypeHasCondition -> lastInjurerByTypeHasCondition.injurerConditions)
    ).apply(builder, LastInjurerByTypeHasCondition::new));
    private final Map<EntityType<?>, List<Condition>> injurerConditions;

    public LastInjurerByTypeHasCondition(Map<EntityType<?>, List<Condition>> injurerConditions) {
        this.injurerConditions = injurerConditions;
    }

    @Override
    public boolean passes(ConditionContext conditionContext) {
        LivingEntity lastHurtByMob = conditionContext.entity().getLastHurtByMob();

        if (lastHurtByMob == null) {
            return false;
        }
        EntityType<?> lastHurtByMobType = lastHurtByMob.getType();
        if (injurerConditions.containsKey(lastHurtByMobType)) {
            List<Condition> conditions = injurerConditions.get(lastHurtByMobType);

            for (Condition condition : conditions) {
                if (!condition.passes(new ConditionContext(conditionContext, lastHurtByMob))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }
}
