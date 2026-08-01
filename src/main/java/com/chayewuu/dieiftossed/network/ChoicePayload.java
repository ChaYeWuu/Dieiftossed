package com.chayewuu.dieiftossed.network;

import com.chayewuu.dieiftossed.DieIfTossed;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

// 客户端→服务端：玩家在确认 GUI 中做出的选择
public record ChoicePayload(ChoiceType choice, Scene scene) implements CustomPacketPayload {
    public static final Identifier ID =
            Identifier.fromNamespaceAndPath(DieIfTossed.MOD_ID, "choice");
    public static final CustomPacketPayload.Type<ChoicePayload> TYPE =
            new CustomPacketPayload.Type<>(ID);

    // 选择类型：死/不死/听天由命
    public enum ChoiceType {
        DIE,
        SPARE,
        RANDOM
    }

    // 场景标记：丢物确认/死亡确认
    public enum Scene {
        DROP,
        DEATH
    }

    // 使用两个 int（枚举 ordinal）序列化
    public static final StreamCodec<RegistryFriendlyByteBuf, ChoicePayload> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT, p -> p.choice.ordinal(),
                    ByteBufCodecs.INT, p -> p.scene.ordinal(),
                    (choiceOrd, sceneOrd) -> new ChoicePayload(
                            ChoiceType.values()[choiceOrd],
                            Scene.values()[sceneOrd])
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
