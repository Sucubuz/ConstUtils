/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package breedableloli.lolihack.modules;

import breedableloli.lolihack.LoliHackAddon;
import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.mixininterface.IVec3d;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.movement.Anchor;
import meteordevelopment.meteorclient.systems.modules.movement.speed.modes.Strafe;
import meteordevelopment.meteorclient.systems.modules.movement.speed.modes.Vanilla;
import meteordevelopment.meteorclient.systems.modules.world.Timer;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.Blocks;

public class SpeedPlus extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<Double> vanillaSpeed = sgGeneral.add(new DoubleSetting.Builder()
            .name("vanilla-speed")
            .description("The speed in blocks per second.")
            .defaultValue(7.5)
            .min(0)
            .sliderMax(20)
            .build());

    public final Setting<Double> timer = sgGeneral.add(new DoubleSetting.Builder()
            .name("timer")
            .description("Timer override.")
            .defaultValue(1)
            .min(0.01)
            .sliderMin(0.01)
            .sliderMax(10)
            .build());

    public final Setting<Boolean> inLiquids = sgGeneral.add(new BoolSetting.Builder()
            .name("in-liquids")
            .description("Uses speed when in lava or water.")
            .defaultValue(false)
            .build());

    public final Setting<Boolean> whenSneaking = sgGeneral.add(new BoolSetting.Builder()
            .name("when-sneaking")
            .description("Uses speed when sneaking.")
            .defaultValue(false)
            .build());

    public final Setting<Boolean> vanillaOnGround = sgGeneral.add(new BoolSetting.Builder()
            .name("only-on-ground")
            .description("Uses speed only when standing on a block.")
            .defaultValue(false)
            .build());

    public SpeedPlus() {
        super(LoliHackAddon.CATEGORY, "speed+", "Speed made for constantiam");

    }

    @Override
    public void onDeactivate() {
        Modules.get().get(Timer.class).setOverride(Timer.OFF);
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {
        if (event.movement.y != -0.0784000015258789) {
            return;
        }
        if (event.type != MovementType.SELF || stopSpeed())
            return;

        if (timer.get() != Timer.OFF) {
            Modules.get().get(Timer.class).setOverride(PlayerUtils.isMoving() ? timer.get() : Timer.OFF);
        }
        Vec3d vel = PlayerUtils.getHorizontalVelocity(this.vanillaSpeed.get());
        double velX = vel.getX();
        double velZ = vel.getZ();

        if (mc.player.hasStatusEffect(StatusEffects.SPEED)) {
            double value = (mc.player.getStatusEffect(StatusEffects.SPEED).getAmplifier() + 1) * 0.205;
            velX += velX * value;
            velZ += velZ * value;
        }

        Anchor anchor = Modules.get().get(Anchor.class);
        if (anchor.isActive() && anchor.controlMovement) {
            velX = anchor.deltaX;
            velZ = anchor.deltaZ;
        }

        ((IVec3d) event.movement).meteor$set(velX, event.movement.y, velZ);
    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        if (stopSpeed())
            return;
    }

    private boolean stopSpeed() {
        if (mc.player.isGliding() || mc.player.isClimbing() || mc.player.getVehicle() != null)
            return true;
        if (!whenSneaking.get() && mc.player.isSneaking())
            return true;
        if (vanillaOnGround.get() && !mc.player.isOnGround())
            return true;

        if (mc.world.getBlockState(mc.player.getBlockPos()).getBlock() == Blocks.SWEET_BERRY_BUSH) {
            return true;
        }
        return !inLiquids.get() && (mc.player.isTouchingWater() || mc.player.isInLava());
    }

}
