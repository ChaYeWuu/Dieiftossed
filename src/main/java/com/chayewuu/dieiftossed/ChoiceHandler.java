package com.chayewuu.dieiftossed;

import com.chayewuu.dieiftossed.network.ChoicePayload;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.concurrent.ThreadLocalRandom;

// 处理客户端发来的玩家选择（死/不死/听天由命）
public class ChoiceHandler {

    public static void handle(ServerPlayer player, ChoicePayload.ChoiceType choice, ChoicePayload.Scene scene) {
        switch (choice) {
            case DIE -> {
                player.level().playSound(null, player, ModSounds.EA, net.minecraft.sounds.SoundSource.PLAYERS, 1.0f, 1.0f);
                handleDie(player);
            }
            case SPARE -> handleSpare(player, scene);
            case RANDOM -> {
                // 听天由命：50% 死，50% 不死
                boolean fateIsDeath = ThreadLocalRandom.current().nextBoolean();
                String fateMsg = fateIsDeath ? "上帝已抉择你的命运：死亡！" : "上帝已抉择你的命运：存活！";
                player.sendOverlayMessage(Component.literal(fateMsg));
                if (fateIsDeath) {
                    player.level().playSound(null, player, ModSounds.EA, net.minecraft.sounds.SoundSource.PLAYERS, 1.0f, 1.0f);
                    handleDie(player);
                } else {
                    player.level().playSound(null, player, ModSounds.NAILONG_BAOBAO, net.minecraft.sounds.SoundSource.PLAYERS, 1.0f, 1.0f);
                    handleSpare(player, scene);
                }
            }
        }
    }

    // 死：标记保留物品栏，加入 PENDING_DEATH 让 ALLOW_DEATH 放行，然后 kill 触发死亡
    private static void handleDie(ServerPlayer player) {
        DieIfTossed.KEEP_INVENTORY_SET.add(player.getUUID());
        DieIfTossed.PENDING_DEATH.add(player.getUUID());
        player.invulnerableTime = 0;
        ServerLevel serverLevel = (ServerLevel) player.level();
        player.kill(serverLevel);
    }

    // 不死：丢物场景无操作；死亡场景复活玩家
    private static void handleSpare(ServerPlayer player, ChoicePayload.Scene scene) {
        if (scene == ChoicePayload.Scene.DEATH) {
            player.setHealth(player.getMaxHealth());
            DieIfTossed.PENDING_DEATH.remove(player.getUUID());
        }
    }
}
