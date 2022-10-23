package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class BiomeTagCondition implements Condition {

    public static final Codec<BiomeTagCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(TagKey.codec(Registry.BIOME_REGISTRY).listOf().fieldOf("biome_tag_is").forGetter(biomeTagCondition -> new ArrayList<>(biomeTagCondition.biomeTags))).apply(builder, BiomeTagCondition::new));
    private final Set<TagKey<Biome>> biomeTags;

    public BiomeTagCondition(Collection<TagKey<Biome>> biomeTags) {
        this.biomeTags = new ObjectOpenHashSet<>(biomeTags);
    }

    @Override
    public boolean passes(ConditionContext conditionContext) {
        Holder<Biome> biome = conditionContext.world().getBiome(conditionContext.entity().blockPosition());
        for (TagKey<Biome> biomeTag : this.biomeTags) {
            if(biome.is(biomeTag)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }
}
