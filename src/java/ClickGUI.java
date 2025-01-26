package org.example.mr_yinkina.yinkinapro.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.example.mr_yinkina.yinkinapro.module.Category;
import org.example.mr_yinkina.yinkinapro.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Screen {
    private final List<Panel> panels;
    private Panel draggingPanel = null;
    private int lastMouseX, lastMouseY;

    public ClickGUI() {
        super(Text.literal("ClickGUI"));
        panels = new ArrayList<>();
        int x = 5;
        for (Category category : Category.values()) {
            panels.add(new Panel(category, x, 5, 100));
            x += 110;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // 渲染半透明背景
        context.fill(0, 0, this.width, this.height, new Color(0, 0, 0, 120).getRGB());
        
        // 渲染所有面板
        for (Panel panel : panels) {
            panel.render(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // 不渲染背景
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // 从后往前检查，这样最上面的面板会优先响应点击
        for (int i = panels.size() - 1; i >= 0; i--) {
            Panel panel = panels.get(i);
            if (panel.mouseClicked((int)mouseX, (int)mouseY, button)) {
                if (button == 0 && panel.isHeaderHovered((int)mouseX, (int)mouseY)) {
                    draggingPanel = panel;
                    lastMouseX = (int)mouseX;
                    lastMouseY = (int)mouseY;
                    
                    // 将点击的面板移到最前面
                    if (i < panels.size() - 1) {
                        panels.remove(i);
                        panels.add(panel);
                    }
                }
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            draggingPanel = null;
        }
        for (Panel panel : panels) {
            panel.mouseReleased((int)mouseX, (int)mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingPanel != null) {
            int newX = (int)(draggingPanel.getX() + mouseX - lastMouseX);
            int newY = (int)(draggingPanel.getY() + mouseY - lastMouseY);
            
            // 限制面板不超出屏幕
            newX = Math.max(0, Math.min(this.width - draggingPanel.getWidth(), newX));
            newY = Math.max(0, Math.min(this.height - draggingPanel.getHeaderHeight(), newY));
            
            draggingPanel.setX(newX);
            draggingPanel.setY(newY);
            
            lastMouseX = (int)mouseX;
            lastMouseY = (int)mouseY;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (Panel panel : panels) {
            if (panel.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
} 