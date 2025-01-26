package org.example.mr_yinkina.yinkinapro.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.example.mr_yinkina.yinkinapro.YinKinaPro;
import org.example.mr_yinkina.yinkinapro.module.Category;
import org.example.mr_yinkina.yinkinapro.module.Module;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Panel {
    private final Category category;
    private int x, y;
    private final int width;
    private final int headerHeight;
    private final List<ModuleButton> buttons;
    private final MinecraftClient mc;
    private boolean expanded = true;
    private float animationProgress;

    public Panel(Category category, int x, int y, int width) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.headerHeight = 15;
        this.buttons = new ArrayList<>();
        this.mc = MinecraftClient.getInstance();
        this.animationProgress = 0;

        int yOffset = headerHeight;
        for (Module module : YinKinaPro.INSTANCE.moduleManager.getModulesByCategory(category)) {
            buttons.add(new ModuleButton(module, this, yOffset));
            yOffset += 15;
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // 渲染面板背景
        Color backgroundColor = new Color(17, 17, 17);
        context.fill(x, y, x + width, y + headerHeight + (int)(buttons.size() * 15 * animationProgress), backgroundColor.getRGB());
        
        // 渲染标题栏背景
        Color titleBgColor = new Color(24, 24, 24);
        context.fill(x, y, x + width, y + headerHeight, titleBgColor.getRGB());
        
        // 渲染标题栏边框
        if (isHeaderHovered(mouseX, mouseY)) {
            context.fill(x, y, x + width, y + 1, new Color(0, 150, 255).getRGB());
            context.fill(x, y + headerHeight - 1, x + width, y + headerHeight, new Color(0, 150, 255, 100).getRGB());
        }
        
        // 渲染标题
        String title = category.getName();
        int titleWidth = mc.textRenderer.getWidth(title);
        float titleX = x + (width - titleWidth) / 2.0f;
        
        // 渲染标题文本
        context.drawText(mc.textRenderer, title, (int)titleX, y + 3, Color.WHITE.getRGB(), false);
        
        // 渲染展开/收起指示器
        String indicator = expanded ? "▼" : "▶";
        context.drawText(mc.textRenderer, indicator, x + width - 12, y + 3, Color.LIGHT_GRAY.getRGB(), false);
        
        // 渲染边框
        context.fill(x, y, x + 1, y + headerHeight + (int)(buttons.size() * 15 * animationProgress), new Color(35, 35, 35).getRGB());
        context.fill(x + width - 1, y, x + width, y + headerHeight + (int)(buttons.size() * 15 * animationProgress), new Color(35, 35, 35).getRGB());
        context.fill(x, y + headerHeight + (int)(buttons.size() * 15 * animationProgress) - 1, x + width, y + headerHeight + (int)(buttons.size() * 15 * animationProgress), new Color(35, 35, 35).getRGB());
        
        // 渲染按钮
        if (animationProgress > 0) {
            for (ModuleButton button : buttons) {
                button.render(context, mouseX, mouseY, delta);
            }
        }
        
        // 更新动画
        if (expanded && animationProgress < 1) {
            animationProgress = Math.min(1, animationProgress + 0.2f);
        } else if (!expanded && animationProgress > 0) {
            animationProgress = Math.max(0, animationProgress - 0.2f);
        }
    }

    private int getHeight() {
        return headerHeight + (int)(buttons.size() * 15 * animationProgress);
    }

    private int getContentHeight() {
        return buttons.size() * 15;
    }

    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (isHeaderHovered(mouseX, mouseY)) {
            if (button == 1) { // 右键点击
                expanded = !expanded;
                return true;
            }
        }

        if (expanded && animationProgress > 0.9f) {
            for (ModuleButton moduleButton : buttons) {
                if (moduleButton.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void mouseReleased(int mouseX, int mouseY, int button) {
        if (expanded) {
            for (ModuleButton moduleButton : buttons) {
                moduleButton.mouseReleased(mouseX, mouseY, button);
            }
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (expanded) {
            for (ModuleButton moduleButton : buttons) {
                if (moduleButton.keyPressed(keyCode, scanCode, modifiers)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isHeaderHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + headerHeight;
    }

    public int getButtonCount() {
        return buttons.size();
    }

    public boolean isExpanded() {
        return expanded;
    }

    public int getHeaderHeight() {
        return headerHeight;
    }

    public int getWidth() {
        return width;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
} 