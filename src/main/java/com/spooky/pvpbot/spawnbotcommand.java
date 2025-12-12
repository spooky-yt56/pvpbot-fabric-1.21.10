package com.spooky.pvpbot;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorld;

import static net.minecraft.server.command.CommandManager.literal;

public class SpawnBotCommand implements CommandRegistrationCallback {
    @Override
    public void register(net.minecraft.server.command.CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(
            literal("pvpbot")
            .then(CommandManager.literal("spawn").executes(ctx -> {
                ServerCommandSource src = ctx.getSource();
                ServerWorld world = src.getWorld();
                BlockPos pos = src.getPlayer().getBlockPos().add(0, 0, 2);

                BotEntity bot = new BotEntity(PvPBotMod.BOT_ENTITY_TYPE, world);
                bot.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5f, 0, 0);
                world.spawnEntity(bot);

                src.sendFeedback(new LiteralText("Spawned PvP bot"), false);
                return 1;
            }))
        );
    }
}
