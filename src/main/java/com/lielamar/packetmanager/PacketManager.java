package com.lielamar.packetmanager;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class PacketManager {

    private static Class<?> craftPlayerClass = null;
    private static Method getHandleMethod = null;
    private static Field connectionField = null;
    private static Method sendPacketMethod = null;

    private static Class<?> particleClass = null;
    private static Method particleMethod = null;
    private static Class<?> enumParticleClass = null;
    private static Method enumParticleMethod = null;
    private static Class<?> packetPlayOutWorldParticlesClass = null;
    private static Constructor<?> packetPlayOutWorldParticlesConstructor = null;

    private static Class<?> iChatBaseComponentClass = null;
    private static Method chatSerializerMethod = null;
    private static Class<?> chatMessageTypeClass = null;
    private static Method chatMessageTypeMethod = null;
    private static Class<?> packetPlayOutChatClass = null;
    private static Constructor<?> packetPlayOutChatConstructor = null;

    private static Class<?> enumTitleActionClass = null;
    private static Method enumTitleActionMethod = null;
    private static Class<?> packetPlayOutTitleClass = null;
    private static Constructor<?> packetPlayOutTitleConstructor = null;

    private static Class<?> entityInsentientClass = null;
    private static Class<?> entityTypesClass = null;
    private static Field[] entityTypesFields = null;
    private static Method entityTypesAMethod = null;

    /**
     * Sends a particle packet to a player
     *
     * @param player                  player to send the packet to
     * @param particleEffect          particle effect to send
     * @param location                location of the particle
     * @param xOffset                 xOffset of the particle
     * @param yOffset                 yOffset of the particle
     * @param zOffset                 zOffset of the particle
     * @param speed                   speed of the particle
     * @param amount                  amount of particles
     */
    public static void sendParticle(Player player, ParticleEffect particleEffect, Location location, float xOffset, float yOffset, float zOffset, float speed, int amount) {
        try {
            if(packetPlayOutWorldParticlesClass == null)
                packetPlayOutWorldParticlesClass = getNMSClass("PacketPlayOutWorldParticles");

            Object packet;

            if(ServerVersion.getInstance().above(ServerVersion.Version.v1_15_R1)) { // 1.15 (or above) constructor
                if(particleClass == null)
                    particleClass = getNMSClass("Particle");

                if(particleMethod == null)
                    particleMethod = particleClass.getMethod("value", (Class<?>) null);

                if(packetPlayOutWorldParticlesConstructor == null)
                    packetPlayOutWorldParticlesConstructor = packetPlayOutWorldParticlesClass.getConstructor(particleClass, boolean.class, double.class, double.class, double.class, float.class, float.class, float.class, float.class, int.class, int[].class);

                Object particle = particleMethod.invoke(particleEffect.toString());
                packet = packetPlayOutWorldParticlesConstructor.newInstance(particle, true, location.getX(), location.getY(), location.getZ(), xOffset, yOffset, zOffset, speed, amount);
            } else if(ServerVersion.getInstance().above(ServerVersion.Version.v1_13_R1)) { // 1.13 (or above) constructor
                if(particleClass == null)
                    particleClass = getNMSClass("Particle");

                if(particleMethod == null)
                    particleMethod = particleClass.getMethod("value", (Class<?>) null);

                if(packetPlayOutWorldParticlesConstructor == null)
                    packetPlayOutWorldParticlesConstructor = packetPlayOutWorldParticlesClass.getConstructor(particleClass, boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class, int[].class);

                Object particle = particleMethod.invoke(particleEffect.toString());
                packet = packetPlayOutWorldParticlesConstructor.newInstance(particle, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), xOffset, yOffset, zOffset, speed, amount);
            } else { // 1.8 (or above) constructor
                if(enumParticleClass == null)
                    enumParticleClass = getNMSClass("EnumParticle");

                if(enumParticleMethod == null)
                    enumParticleMethod = enumParticleClass.getMethod("value", (Class<?>) null);

                if(packetPlayOutWorldParticlesConstructor == null)
                    packetPlayOutWorldParticlesConstructor = packetPlayOutWorldParticlesClass.getConstructor(enumParticleClass, boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class, int[].class);

                Object particle = enumParticleMethod.invoke(particleEffect.toString());
                packet = packetPlayOutWorldParticlesConstructor.newInstance(particle, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), xOffset, yOffset, zOffset, speed, amount, new int[] {});
            }

            sendPacket(player, packet);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an ActionBar packet to a player
     *
     * @param player                  player to send the packet to
     * @param message                 ActionBar message to send
     */
    public static void sendActionbar(Player player, String message) {
        try {
            if(iChatBaseComponentClass == null)
                iChatBaseComponentClass = getNMSClass("IChatBaseComponent");

            if(chatSerializerMethod == null)
                chatSerializerMethod = iChatBaseComponentClass.getClasses()[0].getDeclaredMethod("a", String.class);


            if(packetPlayOutChatClass == null)
                packetPlayOutChatClass = getNMSClass("PacketPlayOutChat");

            Object packet;
            Object chatSerializerObject = chatSerializerMethod.invoke(null, "{\"text\": \"" + message + "\"}");

            if(ServerVersion.getInstance().above(ServerVersion.Version.v1_16_R1)) {
                // 1.16 (or above)
                if(chatMessageTypeClass == null)
                    chatMessageTypeClass = getNMSClass("ChatMessageType");

                if(chatMessageTypeMethod == null)
                    chatMessageTypeMethod = chatMessageTypeClass.getMethod("value", (Class<?>) null);

                if(packetPlayOutChatConstructor == null)
                    packetPlayOutChatConstructor = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass, chatMessageTypeClass, player.getUniqueId().getClass());

                Object chatMessageTypeObject = chatMessageTypeMethod.invoke("GAME_INFO");
                packet = packetPlayOutChatConstructor.newInstance(chatSerializerObject, chatMessageTypeObject, player.getUniqueId());
            } else if(ServerVersion.getInstance().above(ServerVersion.Version.v1_12_R1)) {
                // 1.12 (or above)
                if(chatMessageTypeClass == null)
                    chatMessageTypeClass = getNMSClass("ChatMessageType");

                if(chatMessageTypeMethod == null)
                    chatMessageTypeMethod = chatMessageTypeClass.getMethod("value", (Class<?>) null);

                if(packetPlayOutChatConstructor == null)
                    packetPlayOutChatConstructor = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass, chatMessageTypeClass);

                Object chatMessageTypeObject = chatMessageTypeMethod.invoke("GAME_INFO");
                packet = packetPlayOutChatConstructor.newInstance(chatSerializerObject, chatMessageTypeObject);
            } else {
                if(packetPlayOutChatConstructor == null)
                    packetPlayOutChatConstructor = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass, byte.class);

                // 1.8 (or above)
                packet = packetPlayOutChatConstructor.newInstance(chatSerializerObject, (byte)2);
            }

            sendPacket(player, packet);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sens a Title & Subtitle packet to a player
     *
     * @param player                  player to send the packet to
     * @param title                   Title message to send
     * @param subtitle                Subtitle message to send
     * @param fadeIn                  Time of fade-in                 (ticks)
     * @param showTime                Time of show                    (ticks)
     * @param fadeOut                 Time of fade-out                (ticks)
     */
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int showTime, int fadeOut) {
        try {
            if(iChatBaseComponentClass == null)
                iChatBaseComponentClass = getNMSClass("IChatBaseComponent");

            if(chatSerializerMethod == null)
                chatSerializerMethod = iChatBaseComponentClass.getClasses()[0].getDeclaredMethod("a", String.class);

            if(enumTitleActionClass == null)
                enumTitleActionClass = packetPlayOutTitleClass.getDeclaredClasses()[0];

            if(enumTitleActionMethod == null)
                enumTitleActionMethod = enumTitleActionClass.getMethod("value", (Class<?>) null);

            if(packetPlayOutTitleClass == null)
                packetPlayOutTitleClass = getNMSClass("PacketPlayOutTitle");

            if(packetPlayOutTitleConstructor == null)
                packetPlayOutTitleConstructor = packetPlayOutTitleClass.getConstructor(enumTitleActionClass, iChatBaseComponentClass, int.class, int.class, int.class);


            Object packet;
            Object chatSerializerObject;

            if(title != null) {

                Object titleType = enumTitleActionMethod.invoke("TITLE");
                chatSerializerObject = chatSerializerMethod.invoke(null, "{\"text\": \"" + title + "\"}");

                packet = packetPlayOutTitleConstructor.newInstance(titleType, chatSerializerObject, fadeIn, showTime, fadeOut);
                sendPacket(player, packet);
            }

            if(subtitle != null) {
                Object titleType = enumTitleActionMethod.invoke("SUBTITLE");
                chatSerializerObject = chatSerializerMethod.invoke(null, "{\"text\": \"" + subtitle + "\"}");

                packet = packetPlayOutTitleConstructor.newInstance(titleType, chatSerializerObject, fadeIn, showTime, fadeOut);
                sendPacket(player, packet);
            }
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | IllegalArgumentException | SecurityException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers a custom entity
     *
     * @param name             Name of the custom entity
     * @param id               Id of the entity
     * @param nmsClass         The NMS class of the entity (EntityXXX)
     * @param customClass      The custom class of the entity
     */
    public static void registerEntity(String name, int id, Class<?> nmsClass, Class<?> customClass) {
        if(ServerVersion.getInstance().above(ServerVersion.Version.v1_12_R1)) return;      // No need to register entities after 1.12

        try {
            if(entityInsentientClass == null)
                entityInsentientClass = getNMSClass("EntityInsentient");

            if(entityTypesClass == null)
                entityTypesClass = getNMSClass("EntityTypes");

            if(entityTypesFields == null)
                entityTypesFields = entityTypesClass.getDeclaredFields();

            if(entityTypesAMethod == null)
                entityTypesAMethod = entityTypesClass.getDeclaredMethod("a", Class.class, String.class, int.class);

            if(!isInheriting(nmsClass, entityInsentientClass)) return;                      // Class doesn't inherit from EntityInsentient
            if(!isInheriting(customClass, entityInsentientClass)) return;                   // Class doesn't inherit from EntityInsentient

            List<Map<?, ?>> dataMap = new ArrayList<>();

            for(Field f : entityTypesFields) {
                if(f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    f.setAccessible(true);
                    dataMap.add((Map<?, ?>) f.get(null));
                }
            }

            if(dataMap.get(2).containsKey(id)) {
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }

            entityTypesAMethod.setAccessible(true);
            entityTypesAMethod.invoke(null, customClass, name, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Unregisters a custom entity
     *
     * @param name             Name of the custom entity
     * @param id               Id of the entity
     * @param nmsClass         The NMS class of the entity (EntityXXX)
     * @param customClass      The custom class of the entity
     */
    public static void unregisterEntity(String name, int id, Class<?> nmsClass, Class<?> customClass) {
        if(ServerVersion.getInstance().above(ServerVersion.Version.v1_12_R1)) return;      // No need to register entities after 1.12

        try {
            if (entityInsentientClass == null)
                entityInsentientClass = getNMSClass("EntityInsentient");

            if (entityTypesClass == null)
                entityTypesClass = getNMSClass("EntityTypes");

            if (entityTypesFields == null)
                entityTypesFields = entityTypesClass.getDeclaredFields();

            if (entityTypesAMethod == null)
                entityTypesAMethod = entityTypesClass.getDeclaredMethod("a", Class.class, String.class, int.class);

            if(!isInheriting(nmsClass, getNMSClass("EntityInsentient"))) return;     // Class doesn't inherit from EntityInsentient
            if(!isInheriting(customClass, getNMSClass("EntityInsentient"))) return;  // Class doesn't inherit from EntityInsentient

            List<Map<?, ?>> dataMap = new ArrayList<>();
            for(Field f : entityTypesFields) {
                if(f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    f.setAccessible(true);
                    dataMap.add((Map<?, ?>) f.get(null));
                }
            }

            if(dataMap.get(2).containsKey(id)) {
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a packet to a player
     *
     * @param player          Player to send packet to
     * @param packet          Packet to send
     * @return                Whether or not packet was sent
     */
    public static boolean sendPacket(Player player, Object packet) {
        try {
            if(craftPlayerClass == null)
                craftPlayerClass = getClass("org.bukkit.craftbukkit", "entity.CraftPlayer");

            if(getHandleMethod == null)
                getHandleMethod = craftPlayerClass.getMethod("getHandle");

            if(connectionField == null)
                connectionField = craftPlayerClass.getField("playerConnection");

            if(sendPacketMethod == null)
                sendPacketMethod = getNMSClass("PlayerConnection").getMethod("sendPacket", getNMSClass("Packet"));

            Object craftPlayer = getHandleMethod.invoke(player);
            Object playerConnection = connectionField.get(craftPlayer);

            sendPacketMethod.invoke(playerConnection, packet);
            return true;
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * @param className       Name of the class
     * @return                A class object of "className" in the NMS version of the server
     */
    public static Class<?> getNMSClass(String className) {
        String name = "net.minecraft.server." + ServerVersion.getInstance().getVersion() + "." + className;
        Class<?> nmsClass = null;

        try { nmsClass = Class.forName(name); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }

        return nmsClass;
    }

    /**
     * @param packageName         Name of the package
     * @param className           Name of the class
     * @return                    A class object of "packageName.className" in the NMS version of the server
     */
    public static Class<?> getClass(String packageName, String className) {
        String name = packageName + "." + ServerVersion.getInstance().getVersion() + "." + className;
        Class<?> nmsClass = null;

        try { nmsClass = Class.forName(name); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }

        return nmsClass;
    }

    /**
     * @param fieldName        Private field to get
     * @param clazz            Class to get field of
     * @param object           Object to use in order to get the field
     * @return                 Private field
     */
    public static Object getPrivateField(String fieldName, Class<?> clazz, Object object) {
        Field field;
        Object o = null;

        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            o = field.get(object);
        } catch (Exception e) { e.printStackTrace(); }

        return o;
    }

    /**
     * @param clazz               Class to check if inherits from superClass
     * @param superClass          The super class
     * @return                    Whether or not clazz inherits from superClass
     */
    public static boolean isInheriting(Class<?> clazz, Class<?> superClass) {
        Class<?> current = clazz;

        while(current.getSuperclass().getSuperclass() != null) {
            if(current.getSuperclass().getName().equalsIgnoreCase(superClass.getName())) return true;

            current = current.getSuperclass();
        }
        return false;
    }
}
