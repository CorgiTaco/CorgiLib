package corgitaco.corgilib.entity.condition;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public record ConditionContext(Level world, LivingEntity entity, boolean isDeadOrDying, int previousConditionsPassed) {


    public ConditionContext(ConditionContext conditionContext, LivingEntity entity) {
        this(conditionContext.world(), entity, conditionContext.isDeadOrDying(), conditionContext.previousConditionsPassed());
    }
}
