package com.spooky.pvpbot;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public class BotEventHandler implements ServerEntityEvents.EntityDamageCallback {
    @Override
    public boolean interact(LivingEntity entity, DamageSource source, float amount) {
        if (source.getAttacker() instanceof BotEntity bot && entity instanceof LivingEntity target) {
            if (target.isDead() || amount <= 0) {
                bot.onMiss();
            } else {
                bot.onSuccessfulHit();
            }
        }
        return true;
    }
}
