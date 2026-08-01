package com.chayewuu.dieiftossed.mixin;

import com.chayewuu.dieiftossed.DieIfTossed;
import com.chayewuu.dieiftossed.network.OpenDropPromptPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// 丢物检测：玩家扔出物品时触发"丢物确认"GUI（物品仍正常掉落，仅触发弹窗）
// 注入 LivingEntity.drop(3-arg) 捕获玩家丢物事件
@Mixin(LivingEntity.class)
public abstract class PlayerDropMixin {

    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("HEAD"))
    private void dieiftossed$onDrop(ItemStack stack, boolean dropAround, boolean retainOwnership, CallbackInfoReturnable<?> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Player player)) return;
        if (stack.isEmpty()) return;
        if (player.level().isClientSide()) return;
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (!serverPlayer.level().getGameRules().get(DieIfTossed.DROP_PROMPT_RULE)) return;
        ServerPlayNetworking.send(serverPlayer, new OpenDropPromptPayload());
    }
}
