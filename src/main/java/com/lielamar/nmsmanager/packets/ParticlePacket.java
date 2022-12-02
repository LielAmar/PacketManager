package com.lielamar.nmsmanager.packets;

import com.lielamar.nmsmanager.modules.ParticleEffect;
import com.lielamar.nmsmanager.modules.Version;
import com.lielamar.nmsmanager.reflection.NMSConstructor;
import com.lielamar.nmsmanager.reflection.NMSMethod;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class ParticlePacket extends Packet {

    private final ParticleEffect particleEffect;
    private final Location location;
    private final float xOffset, yOffset, zOffset, speed, amount;

    private ParticlePacket(ParticleEffect particleEffect, Location location, float xOffset, float yOffset, float zOffset, float speed, float amount) {
        this.particleEffect = particleEffect;
        this.location = location;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.speed = speed;
        this.amount = amount;
    }

    public static class Builder {

        private ParticleEffect particleEffect;
        private Location location;
        private float xOffset, yOffset, zOffset, speed, amount;

        public Builder() {
            this.particleEffect = null;
            this.location = null;
            this.xOffset = 0;
            this.yOffset = 0;
            this.zOffset = 0;
            this.speed = 0;
            this.amount = 0;
        }

        public Builder(Builder builder) {
            this.particleEffect = builder.particleEffect;
            this.location = builder.location;
            this.xOffset = builder.xOffset;
            this.yOffset = builder.yOffset;
            this.zOffset = builder.zOffset;
            this.speed = builder.speed;
            this.amount = builder.amount;
        }

        public Builder(ParticleEffect particleEffect, Location location, float xOffset, float yOffset, float zOffset, float speed, float amount) {
            this.particleEffect = particleEffect;
            this.location = location;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.zOffset = zOffset;
            this.speed = speed;
            this.amount = amount;
        }


        public Builder setEffect(ParticleEffect particleEffect) {
            this.particleEffect = particleEffect;

            return this;
        }

        public Builder setLocation(Location location) {
            this.location = location;

            return this;
        }

        public Builder setXOffset(float xOffset) {
            this.xOffset = xOffset;

            return this;
        }

        public Builder setYOffset(float yOffset) {
            this.yOffset = yOffset;

            return this;
        }

        public Builder setZOffset(float zOffset) {
            this.zOffset = zOffset;

            return this;
        }

        public Builder setSpeed(float speed) {
            this.speed = speed;

            return this;
        }

        public Builder setAmount(float amount) {
            this.amount = amount;

            return this;
        }


        public ParticlePacket build() {
            return new ParticlePacket(this.particleEffect, this.location, this.xOffset, this.yOffset, this.zOffset, this.speed, this.amount);
        }
    }


    @Override
    public Object toPacket() {
        try {
            Object packet;

            if(Version.getInstance().getNMSVersion().above(Version.NMSVersion.v1_15_R1)) {
                // 1.15 (or above)
                Object particle = NMSMethod.invokeMethod(NMSMethod.PARTICLE_VALUE_OF, particleEffect.toString());
                packet = NMSConstructor.PACKET_PLAY_OUT_WORLD_PARTICLE_1_15.getConstructor().newInstance(particle, true, location.getX(), location.getY(), location.getZ(), xOffset, yOffset, zOffset, speed, amount);
            } else if(Version.getInstance().getNMSVersion().above(Version.NMSVersion.v1_13_R1)) {
                // 1.13 (or above)
                Object particle = NMSMethod.invokeMethod(NMSMethod.PARTICLE_VALUE_OF, particleEffect.toString());
                packet = NMSConstructor.PACKET_PLAY_OUT_WORLD_PARTICLE_1_13.getConstructor().newInstance(particle, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), xOffset, yOffset, zOffset, speed, amount);
            } else {
                // 1.8 (or above)
                Object particle = NMSMethod.invokeMethod(NMSMethod.ENUM_PARTICLE_VALUE_OF, particleEffect.toString());
                packet = NMSConstructor.PACKET_PLAY_OUT_WORLD_PARTICLE_1_8.getConstructor().newInstance(particle, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), xOffset, yOffset, zOffset, speed, amount, new int[] {});
            }

            return packet;
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void sendThroughAPI(Player player) {
        player.spawnParticle(Particle.valueOf(particleEffect.name()), location, (int) amount, xOffset, yOffset, zOffset, speed);
    }
}
