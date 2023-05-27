package corgitaco.corgilib.world.level.feature.gen;

import com.mojang.serialization.Codec;
import corgitaco.corgilib.world.level.RandomTickScheduler;
import corgitaco.corgilib.world.level.feature.gen.configurations.TreeFromStructureNBTConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TreeFromStructureNBTFeature extends Feature<TreeFromStructureNBTConfig> {

    private static final boolean DEBUG = false;

    public TreeFromStructureNBTFeature(Codec<TreeFromStructureNBTConfig> $$0) {
        super($$0);
    }

    @Override
    public boolean place(FeaturePlaceContext<TreeFromStructureNBTConfig> featurePlaceContext) {
        TreeFromStructureNBTConfig config = featurePlaceContext.config();

        BlockStateProvider logProvider = config.logProvider();
        BlockStateProvider leavesProvider = config.leavesProvider();

        WorldGenLevel level = featurePlaceContext.level();
        StructureTemplateManager templateManager = level.getLevel().getStructureManager();
        ResourceLocation baseLocation = config.baseLocation();
        Optional<StructureTemplate> baseTemplateOptional = templateManager.get(baseLocation);
        ResourceLocation canopyLocation = config.canopyLocation();
        Optional<StructureTemplate> canopyTemplateOptional = templateManager.get(canopyLocation);

        if (baseTemplateOptional.isEmpty()) {
            throw noTreePartPresent(baseLocation);
        }
        if (canopyTemplateOptional.isEmpty()) {
            throw noTreePartPresent(canopyLocation);
        }
        StructureTemplate baseTemplate = baseTemplateOptional.get();
        StructureTemplate canopyTemplate = canopyTemplateOptional.get();
        List<StructureTemplate.Palette> basePalettes = baseTemplate.palettes;
        List<StructureTemplate.Palette> canopyPalettes = canopyTemplate.palettes;
        BlockPos origin = featurePlaceContext.origin();
        if (DEBUG) {
            level.setBlock(origin, Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
        }
        RandomSource random = featurePlaceContext.random();
        StructurePlaceSettings placeSettings = new StructurePlaceSettings().setRotation(Rotation.getRandom(random));
        StructureTemplate.Palette trunkBasePalette = placeSettings.getRandomPalette(basePalettes, origin);
        StructureTemplate.Palette randomCanopyPalette = placeSettings.getRandomPalette(canopyPalettes, origin);

        List<StructureTemplate.StructureBlockInfo> center = trunkBasePalette.blocks(Blocks.WHITE_WOOL);

        if (center.isEmpty()) {
            throw new IllegalArgumentException("No trunk central position was specified for structure NBT palette %s. Trunk central position is specified with white wool.".formatted(config.baseLocation()));
        }
        if (center.size() > 1) {
            throw new IllegalArgumentException("There cannot be more than one trunk central position for structure NBT palette %s. Trunk central position is specified with white wool.".formatted(config.baseLocation()));
        }

        BlockPos centerOffset = center.get(0).pos;
        centerOffset = new BlockPos(-centerOffset.getX(), 0, -centerOffset.getZ());


        List<StructureTemplate.StructureBlockInfo> logs = getStructureInfosInStructurePalletteFromBlockList(config.logTarget(), trunkBasePalette);
        List<StructureTemplate.StructureBlockInfo> logBuilders = trunkBasePalette.blocks(Blocks.RED_WOOL);
        if (logBuilders.isEmpty()) {
            throw new UnsupportedOperationException(String.format("\"%s\" is missing log builders.", baseLocation));
        }

        Set<BlockPos> leavePositions = new HashSet<>();
        Set<BlockPos> trunkPositions = new HashSet<>();

        int trunkLength = config.height().sample(random);
        final int maxTrunkBuildingDepth = config.maxLogDepth();

        for (StructureTemplate.StructureBlockInfo logBuilder : logBuilders) {
            BlockPos pos = getModifiedPos(placeSettings, logBuilder, centerOffset, origin);
            if (!isOnGround(config.maxLogDepth(), level, pos, config.growableOn())) {
                return false; // Exit because all positions are not on ground.
            }
        }

        placeTrunk(config, logProvider, leavesProvider, level, origin, random, placeSettings, trunkBasePalette, centerOffset, logs, logBuilders, leavePositions, trunkPositions, maxTrunkBuildingDepth);

        List<StructureTemplate.StructureBlockInfo> canopyAnchor = trunkBasePalette.blocks(Blocks.YELLOW_WOOL);

        if (!canopyAnchor.isEmpty()) {
            if (canopyAnchor.size() > 1) {
                throw new IllegalArgumentException("There cannot be more than one central canopy position. Canopy central position is specified with yellow wool on the trunk palette.");
            }
            placeCanopy(config, logProvider, leavesProvider, level, getModifiedPos(placeSettings, canopyAnchor.get(0), centerOffset, origin), random, placeSettings, randomCanopyPalette, leavePositions, trunkPositions, trunkLength, config.growableOn());
        } else {
            placeCanopy(config, logProvider, leavesProvider, level, origin, random, placeSettings, randomCanopyPalette, leavePositions, trunkPositions, trunkLength, config.growableOn());
        }

        placeTreeDecorations(config.treeDecorators(), level, random, leavePositions, trunkPositions);

        return true;
    }

    public static void placeAdditional(TreeFromStructureNBTConfig config, WorldGenLevel level, BlockPos origin, StructurePlaceSettings placeSettings, StructureTemplate.Palette palette, BlockPos centerOffset) {
        List<StructureTemplate.StructureBlockInfo> additionalBlocks = getStructureInfosInStructurePalletteFromBlockList(config.placeFromNBT(), palette);
        for (StructureTemplate.StructureBlockInfo additionalBlock : additionalBlocks) {
            BlockPos pos = getModifiedPos(placeSettings, additionalBlock, centerOffset, origin);
            level.setBlock(pos, additionalBlock.state, 2);
        }
    }

    public static void placeTrunk(TreeFromStructureNBTConfig config, BlockStateProvider logProvider, BlockStateProvider leavesProvider, WorldGenLevel level, BlockPos origin, RandomSource random, StructurePlaceSettings placeSettings, StructureTemplate.Palette trunkBasePalette, BlockPos centerOffset, List<StructureTemplate.StructureBlockInfo> logs, List<StructureTemplate.StructureBlockInfo> logBuilders, Set<BlockPos> leavePositions, Set<BlockPos> trunkPositions, int maxTrunkBuildingDepth) {
        fillLogsUnder(random, logProvider, level, origin, placeSettings, centerOffset, logBuilders, maxTrunkBuildingDepth, config.growableOn());
        placeLogsWithRotation(logProvider, level, origin, random, placeSettings, centerOffset, logs, trunkPositions);
        placeLeavesWithCalculatedDistanceAndRotation(leavesProvider, level, origin, random, placeSettings, getStructureInfosInStructurePalletteFromBlockList(config.leavesTarget(), trunkBasePalette), leavePositions, centerOffset, config.leavesPlacementFilter());
        placeAdditional(config, level, origin, placeSettings, trunkBasePalette, centerOffset);
    }

    public static void placeCanopy(TreeFromStructureNBTConfig config, BlockStateProvider logProvider, BlockStateProvider leavesProvider, WorldGenLevel level, BlockPos origin, RandomSource random, StructurePlaceSettings placeSettings, StructureTemplate.Palette randomCanopyPalette, Set<BlockPos> leavePositions, Set<BlockPos> trunkPositions, int trunkLength, BlockPredicate groundFilter) {
        List<StructureTemplate.StructureBlockInfo> leaves = getStructureInfosInStructurePalletteFromBlockList(config.leavesTarget(), randomCanopyPalette);
        List<StructureTemplate.StructureBlockInfo> canopyLogs = getStructureInfosInStructurePalletteFromBlockList(config.logTarget(), randomCanopyPalette);
        List<StructureTemplate.StructureBlockInfo> canopyAnchor = randomCanopyPalette.blocks(Blocks.WHITE_WOOL);

        if (canopyAnchor.isEmpty()) {
            throw new IllegalArgumentException("No canopy anchor was specified for structure NBT palette %s. Canopy anchor is specified with white wool.".formatted(config.canopyLocation()));
        }
        if (canopyAnchor.size() > 1) {
            throw new IllegalArgumentException("There cannot be more than one canopy anchor for structure NBT palette %s. Canopy anchor is specified with white wool.".formatted(config.canopyLocation()));
        }

        StructureTemplate.StructureBlockInfo structureBlockInfo = canopyAnchor.get(0);
        BlockPos canopyCenterOffset = structureBlockInfo.pos;
        canopyCenterOffset = new BlockPos(-canopyCenterOffset.getX(), trunkLength, -canopyCenterOffset.getZ());

        List<StructureTemplate.StructureBlockInfo> trunkFillers = new ArrayList<>(randomCanopyPalette.blocks(Blocks.RED_WOOL));
        fillLogsUnder(random, logProvider, level, origin, placeSettings, canopyCenterOffset, trunkFillers, level.getHeight(), groundFilter);


        placeLogsWithRotation(logProvider, level, origin, random, placeSettings, canopyCenterOffset, canopyLogs, trunkPositions);

        placeLeavesWithCalculatedDistanceAndRotation(leavesProvider, level, origin, random, placeSettings, leaves, leavePositions, canopyCenterOffset, config.leavesPlacementFilter());
        placeAdditional(config, level, origin, placeSettings, randomCanopyPalette, canopyCenterOffset);
    }

    public static void placeLogsWithRotation(BlockStateProvider logProvider, WorldGenLevel level, BlockPos origin, RandomSource random, StructurePlaceSettings placeSettings, BlockPos centerOffset, List<StructureTemplate.StructureBlockInfo> logs, Set<BlockPos> trunkPositions) {
        for (StructureTemplate.StructureBlockInfo trunk : logs) {
            BlockPos pos = getModifiedPos(placeSettings, trunk, centerOffset, origin);
            level.setBlock(pos, getTransformedState(logProvider.getState(random, pos), trunk.state, placeSettings.getRotation()), 2);
            trunkPositions.add(pos);
        }
    }

    public static void placeTreeDecorations(Iterable<TreeDecorator> treeDecorators, WorldGenLevel level, RandomSource random, Set<BlockPos> leavePositions, Set<BlockPos> trunkPositions) {
        for (TreeDecorator treeDecorator : treeDecorators) {
            treeDecorator.place(new TreeDecorator.Context(level, (pos, state) -> level.setBlock(pos, state, 2), random, trunkPositions, leavePositions, new HashSet<>()));
        }
    }

    public static void placeLeavesWithCalculatedDistanceAndRotation(BlockStateProvider leavesProvider, WorldGenLevel level, BlockPos origin, RandomSource random, StructurePlaceSettings placeSettings, List<StructureTemplate.StructureBlockInfo> leaves, Set<BlockPos> leavePositions, BlockPos canopyCenterOffset, BlockPredicate leavesPlacementFilter) {
        List<Runnable> leavesPostApply = new ArrayList<>(leaves.size());
        for (StructureTemplate.StructureBlockInfo leaf : leaves) {
            BlockPos modifiedPos = getModifiedPos(placeSettings, leaf, canopyCenterOffset, origin);

            if (leavesPlacementFilter.test(level, modifiedPos)) {
                BlockState state = leavesProvider.getState(random, modifiedPos);

                if (state.hasProperty(LeavesBlock.DISTANCE) && leaf.state.hasProperty(LeavesBlock.DISTANCE)) {
                    state = state.setValue(LeavesBlock.DISTANCE, leaf.state.getValue(LeavesBlock.DISTANCE));
                }
                if (state.hasProperty(LeavesBlock.WATERLOGGED)) {
                    FluidState fluidState = level.getFluidState(modifiedPos);
                    if (fluidState.is(Fluids.WATER) && fluidState.getAmount() >= 7) {
                        state.setValue(LeavesBlock.WATERLOGGED, true);
                    }
                }

                level.setBlock(modifiedPos, state, 2);
                BlockState finalState = state;
                if (state.hasProperty(LeavesBlock.DISTANCE)) {
                    Runnable postProcess = () -> {
                        BlockState blockState = LeavesBlock.updateDistance(finalState, level, modifiedPos);
                        if (blockState.getValue(LeavesBlock.DISTANCE) < LeavesBlock.DECAY_DISTANCE) {
                            leavePositions.add(modifiedPos);
                            level.setBlock(modifiedPos, blockState, 2);
                            level.scheduleTick(modifiedPos, blockState.getBlock(), 0);
                        } else {
                            level.removeBlock(modifiedPos, false);
                        }
                    };
                    leavesPostApply.add(postProcess);
                } else {
                    leavePositions.add(modifiedPos);
                }
            }
        }
        leavesPostApply.forEach(Runnable::run);
    }

    public static void fillLogsUnder(RandomSource randomSource, BlockStateProvider logProvider, WorldGenLevel level, BlockPos origin, StructurePlaceSettings placeSettings, BlockPos centerOffset, List<StructureTemplate.StructureBlockInfo> logBuilders, int maxTrunkBuildingDepth, BlockPredicate groundFilter) {
        for (StructureTemplate.StructureBlockInfo logBuilder : logBuilders) {
            BlockPos pos = getModifiedPos(placeSettings, logBuilder, centerOffset, origin);
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos().set(pos);

            for (int i = 0; i < maxTrunkBuildingDepth; i++) {
                if (!level.getBlockState(mutableBlockPos).canOcclude()) {
                    if (level instanceof Level) { // Drop the replaced block.
                        level.removeBlock(mutableBlockPos, true);
                    }
                    level.setBlock(mutableBlockPos, logProvider.getState(randomSource, mutableBlockPos), 2);
                    mutableBlockPos.move(Direction.DOWN);
                } else {
                    ((RandomTickScheduler) level.getChunk(mutableBlockPos)).scheduleRandomTick(mutableBlockPos.immutable());
                    break;
                }
            }
        }
    }


    @NotNull
    public static BlockState getTransformedState(BlockState state, BlockState canopyLogState, Rotation rotation) {
        for (Property property : state.getProperties()) {
            if (canopyLogState.hasProperty(property)) {
                Comparable value = canopyLogState.getValue(property);
                state = state.setValue(property, value);
            }
        }

        if (state.hasProperty(RotatedPillarBlock.AXIS)) {
            Direction.Axis axis = state.getValue(RotatedPillarBlock.AXIS);
            if (axis.isHorizontal()) {
                if (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90) {
                    if (axis == Direction.Axis.X) {
                        state = state.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
                    } else if (axis == Direction.Axis.Z) {
                        state = state.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
                    }
                }
            }
        }
        return state;
    }

    public static boolean isOnGround(int maxLogDepth, WorldGenLevel level, BlockPos pos, BlockPredicate growableOn) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos().set(pos);
        for (int logDepth = 0; logDepth < maxLogDepth; logDepth++) {
            mutableBlockPos.move(Direction.DOWN);
            if (growableOn.test(level, mutableBlockPos)) {
                return true;
            }
        }

        return false;
    }

    public static BlockPos getModifiedPos(StructurePlaceSettings settings, StructureTemplate.StructureBlockInfo placing, BlockPos partCenter, BlockPos featureOrigin) {
        return StructureTemplate.calculateRelativePosition(settings, placing.pos).offset(featureOrigin).offset(StructureTemplate.calculateRelativePosition(settings, partCenter));
    }

    public static IllegalArgumentException noTreePartPresent(ResourceLocation location) {
        return new IllegalArgumentException(String.format("\"%s\" is not a valid tree part.", location));
    }

    public static List<StructureTemplate.StructureBlockInfo> getStructureInfosInStructurePalletteFromBlockList(Iterable<Block> blocks, StructureTemplate.Palette palette) {
        List<StructureTemplate.StructureBlockInfo> result = new ArrayList<>();
        for (Block block : blocks) {
            result.addAll(palette.blocks(block));
        }
        return result;
    }
}
