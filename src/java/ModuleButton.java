package org.example.mr_yinkina.yinkinapro.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import org.example.mr_yinkina.yinkinapro.module.Module;

import java.awt.*;

public class ModuleButton {
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final Module module;
    private final Panel parent;
    private final int yOffset;
    private boolean binding;
    private float hoverProgress;
    private float enableProgress;

    public ModuleButton(Module module, Panel parent, int yOffset) {
        this.module = module;
        this.parent = parent;
        this.yOffset = yOffset;
        this.binding = false;
        this.hoverProgress = 0.0f;
        this.enableProgress = module.isEnabled() ? 1.0f : 0.0f;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int x = parent.getX();
        int y = parent.getY() + yOffset;
        
        // 更新悬停动画
        if (isHovered(mouseX, mouseY)) {
            hoverProgress = Math.min(1.0f, hoverProgress + 0.1f);
        } else {
            hoverProgress = Math.max(0.0f, hoverProgress - 0.1f);
        }
        
        // 更新启用动画
        if (module.isEnabled()) {
            enableProgress = Math.min(1.0f, enableProgress + 0.1f);
        } else {
            enableProgress = Math.max(0.0f, enableProgress - 0.1f);
        }
        
        // 渲染按钮背景
        Color bgColor = new Color(22, 22, 22);
        if (isHovered(mouseX, mouseY)) {
            bgColor = new Color(28, 28, 28);
        }
        context.fill(x + 1, y, x + parent.getWidth() - 1, y + 14, bgColor.getRGB());
        
        // 渲染启用指示器
        if (enableProgress > 0) {
            int indicatorWidth = (int)(2 + (enableProgress * 2));
            Color indicatorColor = new Color(0, 150, 255);
            context.fill(x + 1, y, x + indicatorWidth, y + 14, indicatorColor.getRGB());
        }
        
        // 渲染按钮文本
        String buttonText = binding ? "按下按键..." : module.getName();
        float textX = x + 6;
        float textY = y + (14 - 8) / 2.0f;
        
        // 渲染主文本
        int textColor = module.isEnabled() ? 
            Color.WHITE.getRGB() : 
            new Color(170, 170, 170).getRGB();
        context.drawText(mc.textRenderer, buttonText, (int)textX, (int)textY, textColor, false);
        
        // 如果有绑定的按键，渲染按键名称
        if (module.getKey() != -1 && !binding) {
            String keyName = getKeyDisplayName(module.getKey());
            int keyWidth = mc.textRenderer.getWidth(keyName);
            float keyX = x + parent.getWidth() - keyWidth - 6;
            
            // 渲染按键文本
            context.drawText(mc.textRenderer, keyName, 
                (int)keyX, (int)textY, 
                new Color(120, 120, 120).getRGB(), false);
        }
    }

    private String getKeyDisplayName(int keyCode) {
        if (keyCode < 0) return "";
        String keyName = InputUtil.Type.KEYSYM.createFromCode(keyCode).getLocalizedText().getString();
        // 翻译常用按键名称
        return switch (keyName.toLowerCase()) {
            case "right shift" -> "右Shift";
            case "left shift" -> "左Shift";
            case "right control" -> "右Ctrl";
            case "left control" -> "左Ctrl";
            case "right alt" -> "右Alt";
            case "left alt" -> "左Alt";
            case "enter" -> "回车";
            case "escape" -> "Esc";
            case "space" -> "空格";
            case "tab" -> "Tab";
            case "delete" -> "删除";
            case "home" -> "Home";
            case "end" -> "End";
            case "page up" -> "上页";
            case "page down" -> "下页";
            case "insert" -> "插入";
            case "backspace" -> "退格";
            default -> keyName;
        };
    }

    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            switch (button) {
                case 0: // 左键点击
                    module.toggle();
                    return true;
                case 2: // 中键点击
                    binding = true;
                    return true;
            }
        }
        return false;
    }

    public void mouseReleased(int mouseX, int mouseY, int button) {
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (binding) {
            if (keyCode == 256) { // Escape键
                module.setKey(-1);
            } else {
                module.setKey(keyCode);
            }
            binding = false;
            return true;
        }
        return false;
    }

    private boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= parent.getX() && mouseX <= parent.getX() + parent.getWidth() &&
               mouseY >= parent.getY() + yOffset && mouseY <= parent.getY() + yOffset + 15;
    }
} 