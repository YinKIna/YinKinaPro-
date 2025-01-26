package org.example.mr_yinkina.yinkinapro.module.modules.player;

import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.util.Hand;
import org.example.mr_yinkina.yinkinapro.module.Category;
import org.example.mr_yinkina.yinkinapro.module.Module;

public class AutoFishModule extends Module {
    private boolean isFishing = false;
    private long lastCastTime = 0;
    private static final long RECAST_DELAY = 2000; // 增加重新抛竿延迟到2秒
    private int hookTicks = 0;
    private int noMovementTicks = 0; // 记录鱼钩静止的时间
    private boolean waitingForHook = false; // 是否在等待上钩

    public AutoFishModule() {
        super("自动钓鱼", Category.PLAYER);
    }

    @Override
    public void onEnable() {
        isFishing = false;
        lastCastTime = 0;
        hookTicks = 0;
        noMovementTicks = 0;
        waitingForHook = false;
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        // 检查玩家是否手持钓鱼竿
        if (!(mc.player.getMainHandStack().getItem() instanceof FishingRodItem)) {
            return;
        }

        // 获取鱼钩实体
        FishingBobberEntity bobber = mc.player.fishHook;

        // 如果没有在钓鱼且已经过了重新抛竿延迟
        if (!isFishing && System.currentTimeMillis() - lastCastTime > RECAST_DELAY) {
            // 直接抛竿，不需要瞄准
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            isFishing = true;
            lastCastTime = System.currentTimeMillis();
            hookTicks = 0;
            noMovementTicks = 0;
            waitingForHook = false;
            return;
        }

        // 检查鱼钩状态
        if (bobber != null && isFishing) {
            // 获取当前鱼钩速度
            double currentVelocityY = bobber.getVelocity().y;
            
            // 检测鱼钩是否静止（在水中等待）
            if (Math.abs(currentVelocityY) < 0.01) {
                noMovementTicks++;
                if (noMovementTicks > 20) { // 等待1秒确认鱼钩稳定
                    waitingForHook = true;
                }
            } else {
                noMovementTicks = 0;
            }

            // 只有在等待上钩状态时才检测
            if (waitingForHook) {
                // 检测上钩 - 当鱼钩突然向下移动
                if (currentVelocityY < -0.04 || bobber.isRemoved()) {
                    hookTicks++;
                    if (hookTicks >= 2) { // 等待更长时间以确认
                        // 收杆
                        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                        isFishing = false;
                        lastCastTime = System.currentTimeMillis();
                        hookTicks = 0;
                        noMovementTicks = 0;
                        waitingForHook = false;
                    }
                } else {
                    hookTicks = 0;
                }
            }

            // 如果鱼钩不在水中，重新抛竿
            if (!bobber.isInOpenWater()) {
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                isFishing = false;
                lastCastTime = System.currentTimeMillis();
                hookTicks = 0;
                noMovementTicks = 0;
                waitingForHook = false;
            }
        } else {
            isFishing = false;
            hookTicks = 0;
            noMovementTicks = 0;
            waitingForHook = false;
        }
    }
} 