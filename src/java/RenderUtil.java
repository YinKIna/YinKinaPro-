package org.example.mr_yinkina.yinkinapro.util;

import net.minecraft.client.gui.DrawContext;
import java.awt.*;

public class RenderUtil {
    public static void drawRoundedRect(DrawContext context, float x, float y, float width, float height, float radius, Color color) {
        // 填充主体矩形
        context.fill((int)(x + radius), (int)y, (int)(x + width - radius), (int)(y + height), color.getRGB());
        context.fill((int)x, (int)(y + radius), (int)(x + width), (int)(y + height - radius), color.getRGB());
        
        // 填充四个圆角
        drawCircle(context, x + radius, y + radius, radius, color);
        drawCircle(context, x + width - radius, y + radius, radius, color);
        drawCircle(context, x + radius, y + height - radius, radius, color);
        drawCircle(context, x + width - radius, y + height - radius, radius, color);
    }

    public static void drawCircle(DrawContext context, float x, float y, float radius, Color color) {
        float x1 = x - radius;
        float y1 = y - radius;
        float x2 = x + radius;
        float y2 = y + radius;
        
        for (float i = x1; i <= x2; i++) {
            for (float j = y1; j <= y2; j++) {
                float dx = i - x;
                float dy = j - y;
                if (dx * dx + dy * dy <= radius * radius) {
                    context.fill((int)i, (int)j, (int)i + 1, (int)j + 1, color.getRGB());
                }
            }
        }
    }

    public static void drawGradientRect(DrawContext context, float x, float y, float width, float height, Color startColor, Color endColor) {
        for (int i = 0; i < height; i++) {
            float ratio = i / (float) height;
            int r = (int) (startColor.getRed() * (1 - ratio) + endColor.getRed() * ratio);
            int g = (int) (startColor.getGreen() * (1 - ratio) + endColor.getGreen() * ratio);
            int b = (int) (startColor.getBlue() * (1 - ratio) + endColor.getBlue() * ratio);
            int a = (int) (startColor.getAlpha() * (1 - ratio) + endColor.getAlpha() * ratio);
            Color currentColor = new Color(r, g, b, a);
            context.fill((int)x, (int)(y + i), (int)(x + width), (int)(y + i + 1), currentColor.getRGB());
        }
    }

    public static Color rainbow(float speed, float offset) {
        float hue = (System.currentTimeMillis() % (int)(speed * 1000)) / (speed * 1000f);
        hue += offset;
        hue %= 1;
        return Color.getHSBColor(hue, 0.8f, 1.0f);
    }
} 