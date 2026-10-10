package de.codex.spielerumrandung.mixin;

import de.codex.spielerumrandung.OutlineConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityColorMixin {
    @Inject(method = "getTeamColorValue", at = @At("HEAD"), cancellable = true)
    private void spielerumrandung$useCustomColor(CallbackInfoReturnable<Integer> cir) {
        Entity entity = (Entity) (Object) this;
        if (OutlineConfig.enabled && entity instanceof PlayerEntity player && (OutlineConfig.outlineEveryone || OutlineConfig.isFriend(player.getName().getString()))) {
            cir.setReturnValue(OutlineConfig.colorFor(player.getName().getString()));
        }
    }
}
