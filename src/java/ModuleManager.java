package org.example.mr_yinkina.yinkinapro.module;

import org.example.mr_yinkina.yinkinapro.module.modules.player.AutoFishModule;
import org.example.mr_yinkina.yinkinapro.module.modules.exploit.NoMiningDelayModule;
import org.example.mr_yinkina.yinkinapro.module.modules.render.XrayModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private final List<Module> modules;

    public ModuleManager() {
        // 在构造函数中初始化 final 字段
        modules = new ArrayList<>();
        
        // 添加功能模块
        modules.add(new NoMiningDelayModule());
        
        // 添加玩家模块
        modules.add(new AutoFishModule());
        
        // 添加渲染模块
        modules.add(new XrayModule());
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getModulesByCategory(Category category) {
        List<Module> categoryModules = new ArrayList<>();
        for (Module module : modules) {
            if (module.getCategory() == category) {
                categoryModules.add(module);
            }
        }
        return categoryModules;
    }

    public Module getModuleByName(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }
} 