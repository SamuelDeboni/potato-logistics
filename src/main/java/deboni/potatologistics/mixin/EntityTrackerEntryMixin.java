package deboni.potatologistics.mixin;

import deboni.potatologistics.PotatoLogisticsMod;
import net.minecraft.server.entity.EntityTrackerEntry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityFallingSand;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet23VehicleSpawn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = EntityTrackerEntry.class, remap = false)
public class EntityTrackerEntryMixin {
    @Shadow public Entity trackedEntity;

    @Inject(method = "getSpawnPacket", at = @At("HEAD"), cancellable = true)
    private void getSpawnPacket(CallbackInfoReturnable<Packet> cir) {
        if (this.trackedEntity instanceof EntityFallingSand) {
            EntityFallingSand entityfallingsand = (EntityFallingSand)this.trackedEntity;
            if (entityfallingsand.blockID == PotatoLogisticsMod.blockDust.id) {
                cir.setReturnValue(new Packet23VehicleSpawn(this.trackedEntity, 88));
            }
        }
    }
}
