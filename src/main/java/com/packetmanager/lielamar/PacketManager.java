package com.packetmanager.lielamar;

import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class PacketManager {

    private static Method method_getHandle = null;
    static {
        try { method_getHandle = getClass("org.bukkit.craftbukkit", "entity.CraftPlayer").getMethod("getHandle"); }
        catch (NoSuchMethodException e) { e.printStackTrace(); }
    }

    private static Method method_sendPacket = null;
    static {
        try { method_sendPacket = getNMSClass("PlayerConnection").getMethod("sendPacket", getNMSClass("Packet")); }
        catch (NoSuchMethodException e) { e.printStackTrace(); }
    }


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
        Class<?> packetClass = getNMSClass("PacketPlayOutWorldParticles");
        Constructor<?> packetConstructor = null;

        Object packet = null;

        try {
            if(ServerVersion.getInstance().above(ServerVersion.Version.v1_15_R1)) { // 1.15 (or above) constructor
                packetConstructor = packetClass.getConstructor(new Class[] { getNMSClass("Particle"), boolean.class, double.class, double.class, double.class, float.class, float.class, float.class, float.class, int.class, int[].class });
                Object particle = getNMSClass("Particle").getMethod("value", null).invoke(particleEffect.toString());
                packet = packetConstructor.newInstance(particle, true, location.getX(), location.getY(), location.getZ(), xOffset, yOffset, zOffset, speed, amount);
            } else if(ServerVersion.getInstance().above(ServerVersion.Version.v1_13_R1)) { // 1.13 (or above) constructor
                packetConstructor = packetClass.getConstructor(new Class[] { getNMSClass("Particle"), boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class, int[].class });
                Object particle = getNMSClass("Particle").getMethod("value", null).invoke(particleEffect.toString());
                packet = packetConstructor.newInstance(particle, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), xOffset, yOffset, zOffset, speed, amount);
            } else { // 1.8 (or above) constructor
                packetConstructor = packetClass.getConstructor(new Class[] { getNMSClass("EnumParticle"), boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class, int[].class});
                Object particle = getNMSClass("EnumParticle").getField(particleEffect.toString()).get(null);
                packet = packetConstructor.newInstance(particle, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), xOffset, yOffset, zOffset, speed, amount, new int[] {});
            }

            sendPacket(player, packet);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
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
        Class<?> packetClass = getNMSClass("PacketPlayOutChat");
        Class<?> icbcClass = getNMSClass("IChatBaseComponent");

        Object packet;

        try {
            Method chatSerializerMethod = icbcClass.getClasses()[0].getDeclaredMethod("a", new Class[]{ String.class });
            Object chatSerializerObject = chatSerializerMethod.invoke(null, new Object[]{"{\"text\": \"" + message + "\"}"});

            if(ServerVersion.getInstance().above(ServerVersion.Version.v1_12_R1)) { // 1.12 (or above) constructor
                Class chatMessageTypeClass = getNMSClass("ChatMessageType");
                Object chatMessageTypeObject = chatMessageTypeClass.getMethod("value", null).invoke("GAME_INFO");

                Constructor<?> packetConstructor = packetClass.getConstructor(new Class[] { icbcClass, chatMessageTypeClass } );
                packet = packetConstructor.newInstance(new Object[] { chatSerializerObject, chatMessageTypeObject });
            } else { // 1.8 (or above) constructor
                Constructor<?> packetConstructor = packetClass.getConstructor(new Class[] { icbcClass, byte.class } );
                packet = packetConstructor.newInstance(new Object[] { chatSerializerObject, (byte)2 });
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
        Class<?> packetClass = getNMSClass("PacketPlayOutTitle");
        Class<?> icbcClass = getNMSClass("IChatBaseComponent");

        Object packet;

        try {
            Method chatSerializerMethod = icbcClass.getClasses()[0].getDeclaredMethod("a", new Class[]{ String.class });
            Object chatSerializerObject;

            if(title != null) {
                chatSerializerObject = chatSerializerMethod.invoke(null, new Object[] { "{\"text\": \"" + title + "\"}" });

                Class<?> enumTitleAction = packetClass.getDeclaredClasses()[0];
                Object titleType = enumTitleAction.getField("TITLE").get(null);

                Constructor<?> packetConstructor = packetClass.getConstructor(enumTitleAction, icbcClass, int.class, int.class, int.class);
                packet = packetConstructor.newInstance(titleType, chatSerializerObject, fadeIn, showTime, fadeOut);

                sendPacket(player, packet);
            }

            if(subtitle != null) {
                chatSerializerObject = chatSerializerMethod.invoke(null, new Object[] { "{\"text\": \"" + subtitle + "\"}" });

                Class<?> enumTitleAction = packetClass.getDeclaredClasses()[0];
                Object titleType = enumTitleAction.getField("SUBTITLE").get(null);

                Constructor<?> packetConstructor = packetClass.getConstructor(enumTitleAction, icbcClass, int.class, int.class, int.class);
                packet = packetConstructor.newInstance(titleType, chatSerializerObject, fadeIn, showTime, fadeOut);

                sendPacket(player, packet);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
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
        if(!isInheriting(nmsClass, getNMSClass("EntityInsentient"))) return;     // Class doesn't inherit from EntityInsentient
        if(!isInheriting(customClass, getNMSClass("EntityInsentient"))) return;  // Class doesn't inherit from EntityInsentient

        Class<?> entityTypesClass = getNMSClass("EntityTypes");

        try {
            List<Map<?, ?>> dataMap = new ArrayList<>();
            for(Field f : entityTypesClass.getDeclaredFields()) {
                if(f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    f.setAccessible(true);
                    dataMap.add((Map<?, ?>) f.get(null));
                }
            }

            if(dataMap.get(2).containsKey(id)) {
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }

            Method method = entityTypesClass.getDeclaredMethod("a", Class.class, String.class, int.class);
            method.setAccessible(true);
            method.invoke(null, customClass, name, id);
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
        if(!isInheriting(nmsClass, getNMSClass("EntityInsentient"))) return;     // Class doesn't inherit from EntityInsentient
        if(!isInheriting(customClass, getNMSClass("EntityInsentient"))) return;  // Class doesn't inherit from EntityInsentient

        Class<?> entityTypesClass = getNMSClass("EntityTypes");

        try {
            List<Map<?, ?>> dataMap = new ArrayList<>();
            for(Field f : entityTypesClass.getDeclaredFields()) {
                if(f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    f.setAccessible(true);
                    dataMap.add((Map<?, ?>) f.get(null));
                }
            }

            if(dataMap.get(2).containsKey(id)) {
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }

//            Method method = entityTypesClass.getDeclaredMethod("a", Class.class, String.class, int.class);
//            method.setAccessible(true);
//            method.invoke(null, customClass, name, id);
        } catch (Exception e) {
            e.printStackTrace();
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
     * @return
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


    /**
     * Sends a packet to a player
     *
     * @param player          Player to send packet to
     * @param packet          Packet to send
     * @return                Whether or not packet was sent
     */
    public static boolean sendPacket(Player player, Object packet) {
        try {
            Object craftPlayer = method_getHandle.invoke(player);
            Field connectionField = craftPlayer.getClass().getField("playerConnection");
            Object playerConnection = connectionField.get(craftPlayer);

            method_sendPacket.invoke(playerConnection, packet);
            return true;
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return false;
    }
}
