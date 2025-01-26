package org.example.mr_yinkina.yinkinapro.module.modules.exploit;

import org.example.mr_yinkina.yinkinapro.module.Category;
import org.example.mr_yinkina.yinkinapro.module.Module;

public class NoMiningDelayModule extends Module {

    public NoMiningDelayModule() {
        super("快速破坏", Category.EXPLOIT);
    }

    @Override
    public void onTick() {
        if (mc.interactionManager != null) {
            // 移除挖掘冷却
            try {
                java.lang.reflect.Field cooldownField = mc.interactionManager.getClass().getDeclaredField("blockBreakingCooldown");
                cooldownField.setAccessible(true);
                cooldownField.setInt(mc.interactionManager, 0);
            } catch (Exception e) {
                // 忽略可能的错误
            }
        }
    }
} 