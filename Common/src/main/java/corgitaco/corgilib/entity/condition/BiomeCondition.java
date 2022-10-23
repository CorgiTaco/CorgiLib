package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.corgilib.serialization.codec.CodecUtil;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public class BiomeCondition implements Condition {

    public static final Codec<BiomeCondition> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    CodecUtil.BIOME_CODEC.listOf().fieldOf("biome_is").forGetter(biomeCondition -> new ArrayList<>(biomeCondition.biomes))
            ).apply(builder, BiomeCondition::new)
    );
    private final Set<ResourceKey<Biome>> biomes;

    public BiomeCondition(Collection<ResourceKey<Biome>> biomes) {
        this.biomes = new ObjectOpenHashSet<>(biomes);
    }

    @Override
    public boolean passes(ConditionContext conditionContext) {
        Optional<ResourceKey<Biome>> biomeKey = conditionContext.world().getBiome(conditionContext.entity().blockPosition()).unwrapKey();
        return biomeKey.isPresent() && this.biomes.contains(biomeKey.get());
    }

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }
}
