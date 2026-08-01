package com.chayewuu.dieiftossed.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * 渐变文字渲染工具。
 * 将文本按字符从左到右在两个颜色之间线性插值，逐字符绘制（不描边）。
 *
 * 26.2 注意：原 GuiGraphics 已重构为 GuiGraphicsExtractor，
 * drawString 改为 GuiGraphicsExtractor#text(Font, String, int, int, int, boolean)。
 */
public final class GradientTextRenderer {
    // 渐变起始色 #EEC9A3（浅桃），RGB 238,201,163
    public static final int COLOR_START_RGB = 0xEEC9A3;
    // 渐变结束色 #EF629F（粉），RGB 239,98,159
    public static final int COLOR_END_RGB = 0xEF629F;

    private GradientTextRenderer() {
    }

    /**
     * 居中绘制渐变文字。
     *
     * @param graphics    绘制上下文（GuiGraphicsExtractor）
     * @param font        字体
     * @param text        文本
     * @param centerX     居中 X 坐标
     * @param y           Y 坐标
     * @param colorStart  起始颜色（0xAARRGGBB 或 0xRRGGBB，alpha 强制 0xFF）
     * @param colorEnd    结束颜色（0xAARRGGBB 或 0xRRGGBB，alpha 强制 0xFF）
     */
    public static void drawCenteredGradient(GuiGraphicsExtractor graphics, Font font, String text,
                                            int centerX, int y, int colorStart, int colorEnd) {
        if (text == null || text.isEmpty()) {
            return;
        }
        int totalWidth = font.width(text);
        int x = centerX - totalWidth / 2;
        int length = text.length();
        for (int i = 0; i < length; i++) {
            String ch = String.valueOf(text.charAt(i));
            // 第一个字符 ratio=0（起始色），最后一个字符 ratio=1（结束色）
            float ratio = (length == 1) ? 0f : (float) i / (float) (length - 1);
            int color = interpolateArgb(colorStart, colorEnd, ratio);
            graphics.text(font, ch, x, y, color, false);
            x += font.width(ch);
        }
    }

    /**
     * 在两个颜色之间线性插值，输出 0xAARRGGBB，alpha 固定为 0xFF。
     * 输入颜色可为 0xRRGGBB 或 0xAARRGGBB，仅取 RGB 分量参与插值。
     */
    private static int interpolateArgb(int start, int end, float ratio) {
        int startR = (start >> 16) & 0xFF;
        int startG = (start >> 8) & 0xFF;
        int startB = start & 0xFF;
        int endR = (end >> 16) & 0xFF;
        int endG = (end >> 8) & 0xFF;
        int endB = end & 0xFF;
        int r = Math.round(startR + (endR - startR) * ratio);
        int g = Math.round(startG + (endG - startG) * ratio);
        int b = Math.round(startB + (endB - startB) * ratio);
        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }
}
