package com.chayewuu.dieiftossed.client.gui;

import com.chayewuu.dieiftossed.network.ChoicePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * 死亡确认 GUI。
 * 标题翻译键：dieiftossed.death.title（"哈哈你真死了？但你真的想死吗？"）
 * 三个按钮横向居中排列（宽度自适应）：
 * - "死！！！"：ChoicePayload(DIE, DEATH)
 * - "补药啊！我不想死！"：ChoicePayload(SPARE, DEATH)
 * - "听天由命"：ChoicePayload(RANDOM, DEATH)
 *
 * 26.2 适配：Minecraft.setScreen 已改为 setScreenAndShow。
 */
public class DieIfTossedDeathScreen extends BasePromptScreen {
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_SPACING = 10;
    // 按钮最小宽度与左右内边距
    private static final int BUTTON_MIN_WIDTH = 80;
    private static final int BUTTON_PADDING = 20;

    public DieIfTossedDeathScreen() {
        super(Component.translatable("dieiftossed.death.title"));
    }

    @Override
    protected String getTitleText() {
        return Component.translatable("dieiftossed.death.title").getString();
    }

    @Override
    protected void addButtons(int startY) {
        int dieWidth = buttonWidth("dieiftossed.button.die");
        int spareWidth = buttonWidth("dieiftossed.button.spare");
        int fateWidth = buttonWidth("dieiftossed.button.fate");

        int totalWidth = dieWidth + spareWidth + fateWidth + BUTTON_SPACING * 2;
        int x = (this.width - totalWidth) / 2;

        // "死！！！"
        this.addRenderableWidget(Button.builder(Component.translatable("dieiftossed.button.die"), button -> {
            ClientPlayNetworking.send(new ChoicePayload(ChoicePayload.ChoiceType.DIE, ChoicePayload.Scene.DEATH));
            Minecraft.getInstance().setScreenAndShow(null);
        }).bounds(x, startY, dieWidth, BUTTON_HEIGHT).build());
        x += dieWidth + BUTTON_SPACING;

        // "补药啊！我不想死！"
        this.addRenderableWidget(Button.builder(Component.translatable("dieiftossed.button.spare"), button -> {
            ClientPlayNetworking.send(new ChoicePayload(ChoicePayload.ChoiceType.SPARE, ChoicePayload.Scene.DEATH));
            Minecraft.getInstance().setScreenAndShow(null);
        }).bounds(x, startY, spareWidth, BUTTON_HEIGHT).build());
        x += spareWidth + BUTTON_SPACING;

        // "听天由命"
        this.addRenderableWidget(Button.builder(Component.translatable("dieiftossed.button.fate"), button -> {
            ClientPlayNetworking.send(new ChoicePayload(ChoicePayload.ChoiceType.RANDOM, ChoicePayload.Scene.DEATH));
            Minecraft.getInstance().setScreenAndShow(null);
        }).bounds(x, startY, fateWidth, BUTTON_HEIGHT).build());
    }

    /** 按翻译键文字宽度计算按钮宽度，最小 BUTTON_MIN_WIDTH。 */
    private int buttonWidth(String translationKey) {
        int textWidth = this.font.width(Component.translatable(translationKey).getString());
        return Math.max(BUTTON_MIN_WIDTH, textWidth + BUTTON_PADDING);
    }
}
