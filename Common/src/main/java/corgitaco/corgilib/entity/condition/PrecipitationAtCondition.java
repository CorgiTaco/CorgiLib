package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class PrecipitationAtCondition implements Condition {

    public static Codec<PrecipitationAtCondition> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(BlockPos.CODEC.optionalFieldOf("offset", BlockPos.ZERO).forGetter(precipitationAtCondition -> precipitationAtCondition.offset), Codec.BOOL.optionalFieldOf("snow", false).forGetter(precipitationAtCondition -> {
            return precipitationAtCondition.snow;
        })).apply(builder, PrecipitationAtCondition::new);
    });

    private final BlockPos offset;
    private final boolean snow;

    public PrecipitationAtCondition(BlockPos offset, boolean snow) {
        this.offset = offset;
        this.snow = snow;
    }

    @Override
    public boolean passes(ConditionContext conditionContext) {
        BlockPos offset = conditionContext.entity().blockPosition().offset(this.offset);
        Level world = conditionContext.world();
        if (world.isRainingAt(offset)) {
            if (this.snow) {
                return world.getBiome(offset).value().shouldSnow(world, offset);
            }
            return true;
        }
        return false;
    }

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }
}
