package corgitaco.corgilib.mixin;

import corgitaco.corgilib.client.StructureBoxEditor;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MouseHandler.class)
public class MixinMouseHandler {


    @Inject(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getOverlay()Lnet/minecraft/client/gui/screens/Overlay;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void editor(long $$0, double $$1, double $$2, CallbackInfo ci, double increment) {
        if (StructureBoxEditor.onScroll(increment)) {
            ci.cancel();
        }
    }
}