package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class InDimensionCondition implements Condition {

    public static Codec<InDimensionCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(Level.RESOURCE_KEY_CODEC.listOf().fieldOf("dimension_is").forGetter(inDimensionCondition -> new ArrayList<>(inDimensionCondition.validWorlds))
    ).apply(builder, InDimensionCondition::new));
    private final Set<ResourceKey<Level>> validWorlds;

    public InDimensionCondition(Collection<ResourceKey<Level>> validWorlds) {
        this.validWorlds = new ObjectOpenHashSet<>(validWorlds);
    }

    @Override
    public boolean passes(ConditionContext conditionContext) {
        return this.validWorlds.contains(conditionContext.world().dimension());
    }

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }
}
