package com.chayewuu.dieiftossed.client.gui;

import com.chayewuu.dieiftossed.network.ChoicePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * 丢物确认 GUI。
 * 标题翻译键：dieiftossed.drop.title（"你掉落物品了！你死不死？"）
 * 两个按钮横向居中排列：
 * - "死！！！"：发送 ChoicePayload(DIE, DROP)，关闭 Screen
 * - "补药啊！我不想死！"：发送 ChoicePayload(SPARE, DROP)，关闭 Screen
 *
 * 26.2 适配：Minecraft.setScreen 已改为 setScreenAndShow。
 */
public class DieIfTossedDropScreen extends BasePromptScreen {
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_SPACING = 10;

    public DieIfTossedDropScreen() {
        super(Component.translatable("dieiftossed.drop.title"));
    }

    @Override
    protected String getTitleText() {
        return Component.translatable("dieiftossed.drop.title").getString();
    }

    @Override
    protected void addButtons(int startY) {
        int totalWidth = BUTTON_WIDTH * 2 + BUTTON_SPACING;
        int leftX = (this.width - totalWidth) / 2;
        int rightX = leftX + BUTTON_WIDTH + BUTTON_SPACING;

        // "死！！！"
        this.addRenderableWidget(Button.builder(Component.translatable("dieiftossed.button.die"), button -> {
            ClientPlayNetworking.send(new ChoicePayload(ChoicePayload.ChoiceType.DIE, ChoicePayload.Scene.DROP));
            Minecraft.getInstance().setScreenAndShow(null);
        }).bounds(leftX, startY, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        // "补药啊！我不想死！"
        this.addRenderableWidget(Button.builder(Component.translatable("dieiftossed.button.spare"), button -> {
            ClientPlayNetworking.send(new ChoicePayload(ChoicePayload.ChoiceType.SPARE, ChoicePayload.Scene.DROP));
            Minecraft.getInstance().setScreenAndShow(null);
        }).bounds(rightX, startY, BUTTON_WIDTH, BUTTON_HEIGHT).build());
    }
}
