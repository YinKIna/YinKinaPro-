package org.example.mr_yinkina.yinkinapro.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.example.mr_yinkina.yinkinapro.YinKinaPro;
import org.example.mr_yinkina.yinkinapro.module.Module;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public class BlockMixin {
    @Inject(method = "getAmbientOcclusionLightLevel", at = @At("HEAD"), cancellable = true)
    private void onGetAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (YinKinaPro.INSTANCE != null && YinKinaPro.INSTANCE.moduleManager != null) {
            Module xray = YinKinaPro.INSTANCE.moduleManager.getModuleByName("矿物透视");
            if (xray != null && xray.isEnabled() && isOreBlock(state.getBlock())) {
                cir.setReturnValue(1.0f); // 只给矿物方块最大亮度
            }
        }
    }

    @Inject(method = "getOpacity", at = @At("HEAD"), cancellable = true)
    private void onGetOpacity(BlockState state, BlockView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (YinKinaPro.INSTANCE != null && YinKinaPro.INSTANCE.moduleManager != null) {
            Module xray = YinKinaPro.INSTANCE.moduleManager.getModuleByName("矿物透视");
            if (xray != null && xray.isEnabled()) {
                cir.setReturnValue(isOreBlock(state.getBlock()) ? 15 : 0);
            }
        }
    }

    @Inject(method = "isSideInvisible(Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;)Z", at = @At("HEAD"), cancellable = true)
    private void onIsSideInvisible(BlockState state, BlockState stateFrom, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (YinKinaPro.INSTANCE != null && YinKinaPro.INSTANCE.moduleManager != null) {
            Module xray = YinKinaPro.INSTANCE.moduleManager.getModuleByName("矿物透视");
            if (xray != null && xray.isEnabled()) {
                cir.setReturnValue(!isOreBlock(state.getBlock())); // 非矿物方块返回 true 表示不可见
            }
        }
    }

    private static boolean isOreBlock(Block block) {
        return block == Blocks.DIAMOND_ORE ||
               block == Blocks.DEEPSLATE_DIAMOND_ORE ||
               block == Blocks.ANCIENT_DEBRIS ||
               block == Blocks.GOLD_ORE ||
               block == Blocks.DEEPSLATE_GOLD_ORE ||
               block == Blocks.IRON_ORE ||
               block == Blocks.DEEPSLATE_IRON_ORE ||
               block == Blocks.EMERALD_ORE ||
               block == Blocks.DEEPSLATE_EMERALD_ORE ||
               block == Blocks.LAPIS_ORE ||
               block == Blocks.DEEPSLATE_LAPIS_ORE ||
               block == Blocks.REDSTONE_ORE ||
               block == Blocks.DEEPSLATE_REDSTONE_ORE ||
               block == Blocks.NETHER_GOLD_ORE ||
               block == Blocks.NETHER_QUARTZ_ORE ||
               block == Blocks.COPPER_ORE ||
               block == Blocks.DEEPSLATE_COPPER_ORE ||
               block == Blocks.COAL_ORE ||
               block == Blocks.DEEPSLATE_COAL_ORE;
    }
} 