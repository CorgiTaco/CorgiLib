package corgitaco.corgilib.mixin;

import corgitaco.corgilib.entity.IsInsideStructureTracker;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public abstract class MixinEntity implements IsInsideStructureTracker.Access {

    private final IsInsideStructureTracker isInsideStructureTracker = new IsInsideStructureTracker();

    @Override
    public IsInsideStructureTracker getIsInsideStructureTracker() {
        return isInsideStructureTracker;
    }
}
