package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;

import java.util.List;

//TODO: Use vertical anchors in 1.18.
public class YRangeCondition implements Condition {
    public static final Codec<YRangeCondition> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(YRange.CODEC.listOf().fieldOf("y_range_is").forGetter(yRangeCondition -> yRangeCondition.yRanges), BlockPos.CODEC.optionalFieldOf("offset", BlockPos.ZERO).forGetter(yRangeCondition -> yRangeCondition.offset)).apply(builder, YRangeCondition::new);
    });

    private final List<YRange> yRanges;
    private final BlockPos offset;

    public YRangeCondition(List<YRange> yRanges, BlockPos offset) {
        this.yRanges = yRanges;
        this.offset = offset;
        if (yRanges.isEmpty()) {
            throw new IllegalArgumentException("No yRanges were specified.");
        }
    }


    @Override
    public boolean passes(ConditionContext conditionContext) {
        for (YRange yRange : yRanges) {
            if (!yRange.isInBetween(conditionContext.entity().blockPosition().offset(this.offset).getY())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }

    public static class YRange {
        public static Codec<YRange> CODEC = RecordCodecBuilder.create(builder ->
                builder.group(Codec.INT.fieldOf("minY").forGetter(yRange -> yRange.minY),
                Codec.INT.fieldOf("maxY").forGetter(yRange -> yRange.maxY)
        ).apply(builder, YRange::new));

        private final int minY;
        private final int maxY;

        public YRange(int minY, int maxY) {
            this.minY = minY;
            this.maxY = maxY;
            if (minY > maxY) {
                throw new IllegalArgumentException("minY cannot be greater than maxY");
            }
        }

        public boolean isInBetween(int y) {
            return y >= this.minY && y <= this.maxY;
        }
    }
}
