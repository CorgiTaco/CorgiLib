package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.corgilib.serialization.codec.CodecUtil;
import net.minecraft.world.Difficulty;

import java.util.Map;

public class DifficultyCondition implements Condition {

    public static final Codec<DifficultyCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(Codec.unboundedMap(CodecUtil.DIFFICULTY_CODEC, Codec.BOOL).fieldOf("difficulty_is").forGetter(difficultyCondition -> difficultyCondition.isDifficulty)).apply(builder, DifficultyCondition::new));
    private final Map<Difficulty, Boolean> isDifficulty;

    public DifficultyCondition(Map<Difficulty, Boolean> isDifficulty) {
        this.isDifficulty = isDifficulty;
    }

    @Override
    public boolean passes(ConditionContext conditionContext) {
        return this.isDifficulty.getOrDefault(conditionContext.world().getDifficulty(), false);
    }

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }
}
