package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;

public class SeeSkyAtCondition implements Condition {

    public static Codec<SeeSkyAtCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(BlockPos.CODEC.optionalFieldOf("offset", BlockPos.ZERO).forGetter(precipitationAtCondition -> precipitationAtCondition.offset)).apply(builder, SeeSkyAtCondition::new));

    private final BlockPos offset;

    public SeeSkyAtCondition(BlockPos offset) {
        this.offset = offset;
    }

    @Override
    public boolean passes(ConditionContext conditionContext) {
        return conditionContext.world().canSeeSky(conditionContext.entity().blockPosition().offset(this.offset));
    }

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }
}
