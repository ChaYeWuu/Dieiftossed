package com.chayewuu.dieiftossed.client;

import com.chayewuu.dieiftossed.client.gui.DieIfTossedDeathScreen;
import com.chayewuu.dieiftossed.client.gui.DieIfTossedDropScreen;
import com.chayewuu.dieiftossed.network.OpenDeathPromptPayload;
import com.chayewuu.dieiftossed.network.OpenDropPromptPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.chayewuu.dieiftossed.DieIfTossed.MOD_ID;

public class DieIfTossedClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID + "-client");

    @Override
    public void onInitializeClient() {
        // 服务端 -> 客户端：打开丢物确认 GUI
        // 如果当前已有同类 GUI 打开，则不重复打开（避免闪烁）
        ClientPlayNetworking.registerGlobalReceiver(OpenDropPromptPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                Minecraft mc = Minecraft.getInstance();
                // 26.2 中 screen 移到 Gui 类管理，用 mc.gui.screen() 获取当前 screen
                Screen current = mc.gui.screen();
                if (current instanceof DieIfTossedDropScreen) {
                    // 已有丢物确认 GUI 打开，不重复打开
                    return;
                }
                mc.setScreenAndShow(new DieIfTossedDropScreen());
            });
        });

        // 服务端 -> 客户端：打开死亡确认 GUI
        ClientPlayNetworking.registerGlobalReceiver(OpenDeathPromptPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                Minecraft mc = Minecraft.getInstance();
                Screen current = mc.gui.screen();
                if (current instanceof DieIfTossedDeathScreen) {
                    // 已有死亡确认 GUI 打开，不重复打开
                    return;
                }
                mc.setScreenAndShow(new DieIfTossedDeathScreen());
            });
        });

        LOGGER.info("[dieiftossed] 客户端初始化完成。");
    }
}
