package corgitaco.corgilib.mixin.client;

import corgitaco.corgilib.client.imgui.ImguiTest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "render", at = @At("RETURN"))
    private void corgiLib$RenderImGui(float pPartialTicks, long pNanoTime, boolean pRenderLevel, CallbackInfo ci) {
        if(!Minecraft.ON_OSX) {
            ImguiTest.renderTest();
        }
    }
}
