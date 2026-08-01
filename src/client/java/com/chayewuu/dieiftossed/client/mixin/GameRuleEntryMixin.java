package com.chayewuu.dieiftossed.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

// 游戏规则名渐变 Mixin：当规则名是"掉落死亡不？"（或英文"Drop Death Prompt?"）时，
// 用 #EEC9A3 → #EF629F 渐变色逐字符绘制，替代原版白色文字
@Mixin(targets = "net.minecraft.client.gui.screens.worldselection.AbstractGameRulesScreen$GameRuleEntry")
public class GameRuleEntryMixin {

    @Shadow
    private List<FormattedCharSequence> label;

    @Inject(method = "extractLabel", at = @At("HEAD"), cancellable = true)
    private void dieiftossed$onExtractLabel(GuiGraphicsExtractor graphics, int param1, int param2, CallbackInfo ci) {
        if (this.label == null || this.label.size() != 1) return;

        // 遍历 FormattedCharSequence 获取纯文本内容
        StringBuilder sb = new StringBuilder();
        FormattedCharSequence seq = this.label.get(0);
        FormattedCharSink sink = (index, style, codePoint) -> {
            sb.appendCodePoint(codePoint);
            return true;
        };
        seq.accept(sink);
        String text = sb.toString();

        // 仅对"掉落死亡不？"和"Drop Death Prompt?"应用渐变
        if (!text.equals("掉落死亡不？") && !text.equals("Drop Death Prompt?")) return;

        // 取消原版渲染，改用渐变绘制
        ci.cancel();

        Font font = Minecraft.getInstance().font;
        // extractLabel 中 param1 用作 Y 坐标（+5偏移），param2 用作 X 坐标
        int xPos = param2;
        int yPos = param1 + 5;

        // 渐变色：#EEC9A3 → #EF629F（alpha=0xFF 确保不透明）
        int startColor = 0xFFEEC9A3;
        int endColor = 0xFFEF629F;
        int r1 = (startColor >> 16) & 0xFF;
        int g1 = (startColor >> 8) & 0xFF;
        int b1 = startColor & 0xFF;
        int r2 = (endColor >> 16) & 0xFF;
        int g2 = (endColor >> 8) & 0xFF;
        int b2 = endColor & 0xFF;

        // 逐字符绘制，颜色在 startColor 和 endColor 之间线性插值
        int currentX = xPos;
        int charCount = text.length();
        for (int i = 0; i < charCount; i++) {
            String ch = String.valueOf(text.charAt(i));
            float ratio = charCount == 1 ? 0f : (float) i / (charCount - 1);
            int r = (int) (r1 + (r2 - r1) * ratio);
            int g = (int) (g1 + (g2 - g1) * ratio);
            int b = (int) (b1 + (b2 - b1) * ratio);
            int color = (0xFF << 24) | (r << 16) | (g << 8) | b;
            graphics.text(font, ch, currentX, yPos, color, false);
            currentX += font.width(ch);
        }
    }
}
