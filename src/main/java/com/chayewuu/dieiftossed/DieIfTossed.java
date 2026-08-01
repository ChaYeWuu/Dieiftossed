package com.chayewuu.dieiftossed;

import com.chayewuu.dieiftossed.network.ChoicePayload;
import com.chayewuu.dieiftossed.network.OpenDeathPromptPayload;
import com.chayewuu.dieiftossed.network.OpenDropPromptPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DieIfTossed implements ModInitializer {
    public static final String MOD_ID = "dieiftossed";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // 自定义游戏规则：掉落死亡不？默认值固定为 true，归类 PLAYER（显示在"死亡后保留物品栏"下方）
    public static final GameRule<Boolean> DROP_PROMPT_RULE =
            GameRuleBuilder.forBoolean(true)
                    .category(GameRuleCategory.PLAYER)
                    .buildAndRegister(Identifier.fromNamespaceAndPath(MOD_ID, "dieIfTossedDropPrompt"));

    // 保留物品栏标志：放入代表"下次死亡保留物品栏"，由 KeepInventoryMixin 消费
    public static final Set<UUID> KEEP_INVENTORY_SET = Collections.newSetFromMap(new ConcurrentHashMap<>());

    // 待选择死亡标记：区分"被 GUI 询问的死亡"与"选择 DIE 后的强制死亡"，防重入
    public static final Set<UUID> PENDING_DEATH = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public void onInitialize() {
        LOGGER.info("[dieiftossed] 模组初始化完成。你死不死？");

        // 注册自定义音效
        ModSounds.init();

        // 注册网络载荷（服务端→客户端用 clientboundPlay，客户端→服务端用 serverboundPlay）
        PayloadTypeRegistry.clientboundPlay().register(OpenDropPromptPayload.TYPE, OpenDropPromptPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(OpenDeathPromptPayload.TYPE, OpenDeathPromptPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ChoicePayload.TYPE, ChoicePayload.CODEC);

        // 注册 ChoicePayload 接收器，在服务端主线程处理玩家选择
        ServerPlayNetworking.registerGlobalReceiver(ChoicePayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            context.server().execute(() -> ChoiceHandler.handle(player, payload.choice(), payload.scene()));
        });

        // 注册死亡拦截：首次死亡时取消并发送死亡确认 GUI，选择 DIE 后放行
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, damageAmount) -> {
            if (entity instanceof ServerPlayer player) {
                if (PENDING_DEATH.contains(player.getUUID())) {
                    // 玩家已选择死亡（由 ChoiceHandler 触发），放行本次死亡
                    PENDING_DEATH.remove(player.getUUID());
                    return true;
                }
                // 首次死亡：拦截，发送死亡确认 GUI
                PENDING_DEATH.add(player.getUUID());
                // 恢复 1 点生命，防止下一 tick 因血量<=0 再次触发死亡
                player.setHealth(1.0F);
                ServerPlayNetworking.send(player, new OpenDeathPromptPayload());
                return false;
            }
            return true;
        });
    }
}
