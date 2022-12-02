package com.lielamar.nmsmanager.reflection;

import com.lielamar.nmsmanager.ReflectionHelper;

public enum NMSClass {

    CRAFT_PLAYER("org.bukkit.craftbukkit", "entity.CraftPlayer"),

    PARTICLE("net.minecraft.server", "Particle"),
    ENUM_PARTICLE("net.minecraft.server", "EnumParticle"),

    I_CHAT_BASE_COMPONENT("net.minecraft.server", "IChatBaseComponent"),
    I_CHAT_BASE_COMPONENT_SUB_CLASS("net.minecraft.server", "IChatBaseComponent", I_CHAT_BASE_COMPONENT.getClazz().getClasses()[0]),
    CHAT_MESSAGE_TYPE("net.minecraft.server", "ChatMessageType"),

    PACKET("net.minecraft.server", "Packet"),
    PACKET_PLAY_OUT_WORLD_PARTICLE("net.minecraft.server", "PacketPlayOutWorldParticles"),
    PACKET_PLAY_OUT_CHAT("net.minecraft.server", "PacketPlayOutChat"),
    PACKET_PLAY_OUT_TITLE("net.minecraft.server", "PacketPlayOutTitle"),

    ENUM_TITLE_ACTION("net.minecraft.server", "PacketPlayOutTitle", PACKET_PLAY_OUT_TITLE.getClazz().getDeclaredClasses()[0]),

    ENTITY_INSENTIENT("net.minecraft.server", "EntityInsentient"),
    ENTITY_TYPES("net.minecraft.server", "EntityTypes");


    private final String packageName;
    private final String className;

    private Class<?> clazz;

    NMSClass(String packageName, String className) {
        this(packageName, className, null);
    }

    NMSClass(String packageName, String className, Class<?> clazz) {
        this.packageName = packageName;
        this.className = className;
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        if(clazz == null)
            this.clazz = ReflectionHelper.getServerVersionClass(packageName, className);

        return clazz;
    }
}
