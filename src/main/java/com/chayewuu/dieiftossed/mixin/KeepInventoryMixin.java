package com.chayewuu.dieiftossed.mixin;

import com.chayewuu.dieiftossed.DieIfTossed;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// 保留物品栏：玩家选择"死！！！"时，物品栏/经验保留并复制到重生后的新玩家
// - 注入 dropAllDeathLoot HEAD + cancel：跳过整个死亡掉落链（装备+物品栏+经验）
// - 注入 restoreFrom RETURN：手动复制旧玩家物品栏/经验/分数到新玩家
//   （等价于原版私有方法 transferInventoryXpAndScore 的实现）
@Mixin(LivingEntity.class)
public abstract class KeepInventoryMixin {

    @Inject(method = "dropAllDeathLoot(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;)V", at = @At("HEAD"), cancellable = true)
    private void dieiftossed$onDropAllDeathLoot(ServerLevel level, DamageSource source, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Player player)) return;
        if (DieIfTossed.KEEP_INVENTORY_SET.contains(player.getUUID())) {
            ci.cancel();
        }
    }

    @Mixin(ServerPlayer.class)
    public static abstract class RestoreFromMixin {
        @Inject(method = "restoreFrom(Lnet/minecraft/server/level/ServerPlayer;Z)V", at = @At("RETURN"))
        private void dieiftossed$onRestoreFrom(ServerPlayer oldPlayer, boolean keepEverything, CallbackInfo ci) {
            ServerPlayer self = (ServerPlayer) (Object) this;
            if (DieIfTossed.KEEP_INVENTORY_SET.contains(oldPlayer.getUUID())) {
                self.getInventory().replaceWith(oldPlayer.getInventory());
                self.experienceLevel = oldPlayer.experienceLevel;
                self.totalExperience = oldPlayer.totalExperience;
                self.experienceProgress = oldPlayer.experienceProgress;
                self.setScore(oldPlayer.getScore());
                DieIfTossed.KEEP_INVENTORY_SET.remove(oldPlayer.getUUID());
            }
        }
    }
}
