package com.spooky.pvpbot;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.Random;

public class BotGoals {

    public static class PvPBehaviorGoal extends Goal {
        private final PathAwareEntity bot;
        private final double speed;
        private int attackCooldown = 0;
        private final Random rand = new Random();

        public PvPBehaviorGoal(PathAwareEntity bot, double speed) {
            this.bot = bot;
            this.speed = speed;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            return bot.getTarget() instanceof PlayerEntity;
        }

        @Override
        public void tick() {
            if (!(bot.getTarget() instanceof PlayerEntity)) return;
            PlayerEntity target = (PlayerEntity) bot.getTarget();
            double dist = bot.squaredDistanceTo(target);

            double aggression = ((BotEntity) bot).memory.getAggression();
            if (rand.nextInt(20) < (3 + (int)(aggression*10))) {
                Vec3d dir = target.getPos().subtract(bot.getPos()).normalize();
                Vec3d perp = new Vec3d(-dir.z, 0, dir.x).multiply(0.4 * (rand.nextBoolean() ? 1 : -1));
                bot.getNavigation().startMovingTo(target, speed);
                bot.setVelocity(bot.getVelocity().add(perp));
            } else {
                bot.getNavigation().startMovingTo(target, speed);
            }

            bot.getLookControl().lookAt(target, 30.0F, 30.0F);

            if (attackCooldown <= 0) {
                if (dist < 3.0) {
                    bot.tryAttack(target);
                    attackCooldown = 10 + (int)((1.0 - ((BotEntity) bot).memory.getAccuracy()) * 10);
                }
            } else {
                attackCooldown--;
            }
        }
    }
}
