package com.spooky.pvpbot;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PvPBotMod implements ModInitializer {
    public static final String MODID = "pvpbot";
    public static EntityType<BotEntity> BOT_ENTITY_TYPE;

    @Override
    public void onInitialize() {
        BOT_ENTITY_TYPE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MODID, "bot"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BotEntity::new)
                .dimensions(EntityDimensions.fixed(0.6F, 1.95F))
                .trackRangeBlocks(80)
                .trackedUpdateRate(3)
                .build()
        );

        CommandRegistrationCallback.EVENT.register(new SpawnBotCommand());

        // Hook bot hit/miss events
        ServerEntityEvents.ENTITY_DAMAGE.register(new BotEventHandler());
    }
}
