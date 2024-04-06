package deboni.potatologistics.mixin;

import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockModelRenderBlocks.class, remap = false)
public class BlockModelRenderBlocksMixin {
    @Shadow @Final public int renderType;

    @Inject(method = "shouldItemRender3d()Z", at = @At("HEAD"), cancellable = true)
    private void shouldRender3d(CallbackInfoReturnable<Boolean> cir){
        if (renderType == 150 || renderType == 151 || renderType == 152 || renderType == 154 || renderType == 155){
            cir.setReturnValue(true);
        }
    }
}
