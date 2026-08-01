package com.chayewuu.dieiftossed;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

import java.util.Optional;

// 自定义音效注册：听天由命的命运音效
public class ModSounds {
    // 存活音效：奶龙宝宝
    public static final SoundEvent NAILONG_BAOBAO = register("nailongbaobao");
    // 死亡音效：ea
    public static final SoundEvent EA = register("ea");

    private static SoundEvent register(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(DieIfTossed.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, new SoundEvent(id, Optional.empty()));
    }

    public static void init() {
        DieIfTossed.LOGGER.info("[dieiftossed] 音效注册完成。");
    }
}
