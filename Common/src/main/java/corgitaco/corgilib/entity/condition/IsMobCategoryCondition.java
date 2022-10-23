package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.MobCategory;

public record IsMobCategoryCondition(MobCategory category) implements Condition {

    public static final Codec<IsMobCategoryCondition> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    MobCategory.CODEC.fieldOf("mob_category").forGetter(IsMobCategoryCondition::category)
            ).apply(builder, IsMobCategoryCondition::new)
    );

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }

    @Override
    public boolean passes(ConditionContext conditionContext) {
        return conditionContext.entity().getType().getCategory() == this.category;
    }
}
