package org.example.mr_yinkina.yinkinapro.module.modules.render;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.example.mr_yinkina.yinkinapro.module.Category;
import org.example.mr_yinkina.yinkinapro.module.Module;

public class XrayModule extends Module {
    public XrayModule() {
        super("矿物透视", Category.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        // 强制重新渲染世界
        if (mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        // 强制重新渲染世界
        if (mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }
} 