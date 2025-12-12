package com.spooky.pvpbot;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class BotEntity extends PathAwareEntity {
    public AdaptiveMemory memory;

    protected BotEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
        this.memory = new AdaptiveMemory();
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(20.0D);
        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(3.0D);
    }

    public static DefaultAttributeContainer.Builder createBotAttributes() {
        return PathAwareEntity.createMobAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 0.9D));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.goalSelector.add(2, new BotGoals.PvPBehaviorGoal(this, 1.15));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isClient && this.age % 100 == 0) {
            memory.adjustParameters();
            double speed = 0.25 + memory.getAggression() * 0.2;
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
        }
    }

    public void onSuccessfulHit() {
        memory.recordHit(true);
    }

    public void onMiss() {
        memory.recordHit(false);
    }
}
