package org.example.mr_yinkina.yinkinapro.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.example.mr_yinkina.yinkinapro.YinKinaPro;
import org.example.mr_yinkina.yinkinapro.module.Module;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class NoMiningDelayMixin {
    @Inject(method = "getBlockBreakingProgress()I", at = @At("HEAD"), cancellable = true)
    private void onGetBlockBreakingProgress(CallbackInfoReturnable<Integer> cir) {
        Module module = YinKinaPro.INSTANCE.moduleManager.getModuleByName("无挖掘延迟");
        if (module != null && module.isEnabled()) {
            cir.setReturnValue(0);
        }
    }

    @Inject(method = "breakBlock(Lnet/minecraft/util/math/BlockPos;)Z", at = @At("HEAD"))
    private void onBreakBlock(CallbackInfoReturnable<Boolean> cir) {
        Module module = YinKinaPro.INSTANCE.moduleManager.getModuleByName("无挖掘延迟");
        if (module != null && module.isEnabled()) {
            // 不取消方块破坏，但移除延迟
        }
    }

    @Inject(method = "attackBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z", at = @At("HEAD"))
    private void onAttackBlock(CallbackInfoReturnable<Boolean> cir) {
        Module module = YinKinaPro.INSTANCE.moduleManager.getModuleByName("无挖掘延迟");
        if (module != null && module.isEnabled()) {
            // 允许立即开始破坏新方块
        }
    }
} 