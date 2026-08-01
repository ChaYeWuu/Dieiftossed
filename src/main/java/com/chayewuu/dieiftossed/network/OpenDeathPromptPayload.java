package com.chayewuu.dieiftossed.network;

import com.chayewuu.dieiftossed.DieIfTossed;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

// 服务端→客户端：通知客户端打开"死亡确认"GUI（无字段）
public record OpenDeathPromptPayload() implements CustomPacketPayload {
    public static final Identifier ID =
            Identifier.fromNamespaceAndPath(DieIfTossed.MOD_ID, "open_death_prompt");
    public static final CustomPacketPayload.Type<OpenDeathPromptPayload> TYPE =
            new CustomPacketPayload.Type<>(ID);

    // 无字段载荷，编解码为空操作
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenDeathPromptPayload> CODEC =
            StreamCodec.unit(new OpenDeathPromptPayload());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
