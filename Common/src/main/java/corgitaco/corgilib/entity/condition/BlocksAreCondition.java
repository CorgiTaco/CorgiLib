package corgitaco.corgilib.entity.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.corgilib.serialization.codec.CodecUtil;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class BlocksAreCondition implements Condition {

    public static final Codec<BlocksAreCondition> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(BlockIs.CODEC.listOf().fieldOf("has").forGetter(blockStatesAreCondition -> new ArrayList<>(blockStatesAreCondition.blockStatesAre))).apply(builder, BlocksAreCondition::new);
    });

    private final Set<BlockIs> blockStatesAre;

    public BlocksAreCondition(List<BlockIs> blockStatesAre) {
        this.blockStatesAre = new ObjectOpenHashSet<>(blockStatesAre);
    }

    @Override
    public boolean passes(ConditionContext conditionContext) {
        for (BlockIs blockIs : blockStatesAre) {
            BlockPos offsetPos = conditionContext.entity().blockPosition().offset(blockIs.offset);

            if (!blockIs.is.contains(conditionContext.world().getBlockState(offsetPos).getBlock())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Codec<? extends Condition> codec() {
        return CODEC;
    }

    public static class BlockIs {
        public static final Codec<BlockIs> CODEC = RecordCodecBuilder.create(builder -> {
            return builder.group(BlockPos.CODEC.optionalFieldOf("offset", BlockPos.ZERO).forGetter(blockIs -> blockIs.offset),
                    CodecUtil.BLOCK_CODEC.listOf().fieldOf("is").forGetter(blockIs -> new ArrayList<>(blockIs.is))).apply(builder, BlockIs::new);
        });

        private final BlockPos offset;
        private final Set<Block> is;

        public BlockIs(BlockPos offset, Collection<Block> is) {
            this.offset = offset;
            this.is = new ObjectOpenHashSet<>(is);
        }
    }
}