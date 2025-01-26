package org.example.mr_yinkina.yinkinapro.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class Module {
    protected final MinecraftClient mc = MinecraftClient.getInstance();
    private final String name;
    private final Category category;
    private boolean enabled;
    private int key;

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
        this.enabled = false;
        this.key = -1;
    }

    public void onEnable() {
        enabled = true;
    }

    public void onDisable() {
        enabled = false;
    }

    public void onTick() {}

    public void onWorldRender(MatrixStack matrices) {}

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (enabled) {
                onEnable();
            } else {
                onDisable();
            }
        }
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void toggle() {
        enabled = !enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }
} 