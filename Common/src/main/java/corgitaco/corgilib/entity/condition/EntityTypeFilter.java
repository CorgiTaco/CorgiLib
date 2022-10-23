package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.corgilib.serialization.codec.CodecUtil;
import net.minecraft.world.entity.EntityType;

public record EntityTypeFilter(EntityType<?> entityType) implements Condition {

    public static final Codec<EntityTypeFilter> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    CodecUtil.ENTITY_TYPE.fieldOf("id").forGetter(EntityTypeFilter::entityType)
            ).apply(builder, EntityTypeFilter::new)
    );


    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }

    @Override
    public boolean passes(ConditionContext conditionContext) {
        return conditionContext.entity().getType() == this.entityType;
    }
}
