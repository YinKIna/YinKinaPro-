package org.example.mr_yinkina.yinkinapro;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.mr_yinkina.yinkinapro.config.ConfigManager;
import org.example.mr_yinkina.yinkinapro.gui.ClickGUI;
import org.example.mr_yinkina.yinkinapro.module.ModuleManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;

public class YinKinaPro implements ModInitializer {
    public static final String MOD_ID = "yinkinapro";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static YinKinaPro INSTANCE;
    public final ModuleManager moduleManager;
    public ClickGUI clickGUI;
    public ConfigManager configManager;
    private KeyBinding openGuiKey;

    public YinKinaPro() {
        INSTANCE = this;
        moduleManager = new ModuleManager();
        configManager = new ConfigManager();
        clickGUI = new ClickGUI();
        
        // 注册按键绑定
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.yinkinapro.open_gui", // 翻译键
            InputUtil.Type.KEYSYM,     // 键盘按键类型
            GLFW.GLFW_KEY_RIGHT_SHIFT, // 默认按键为右Shift
            "category.yinkinapro.general" // 设置分类
        ));

        // 注册按键处理
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(clickGUI);
                }
            }

            // 更新模块
            if (client.player != null) {
                moduleManager.getModules().forEach(module -> {
                    if (module.isEnabled()) {
                        module.onTick();
                    }
                });
            }
        });
        
        // 注册世界渲染事件
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc != null && mc.player != null) {
                moduleManager.getModules().forEach(module -> {
                    if (module.isEnabled()) {
                        module.onWorldRender(context.matrixStack());
                    }
                });
            }
        });
        
        // 加载配置
        configManager.loadConfig();

        LOGGER.info("YinKinaPro initialized!");
    }

    @Override
    public void onInitialize() {
        LOGGER.info("YinKinaPro initialized!");
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public static MinecraftClient getMinecraft() {
        return MinecraftClient.getInstance();
    }
} 