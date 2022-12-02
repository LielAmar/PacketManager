package com.lielamar.nmsmanager.reflection;

import java.lang.reflect.Constructor;
import java.util.UUID;

public enum NMSConstructor {

    PACKET_PLAY_OUT_WORLD_PARTICLE_1_15(NMSClass.PACKET_PLAY_OUT_WORLD_PARTICLE,
            new Class[] { NMSClass.PARTICLE.getClazz(), boolean.class, double.class, double.class, double.class, float.class, float.class, float.class, float.class, int.class, int[].class }),

    PACKET_PLAY_OUT_WORLD_PARTICLE_1_13(NMSClass.PACKET_PLAY_OUT_WORLD_PARTICLE,
            new Class[] { NMSClass.PARTICLE.getClazz(), boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class, int[].class }),

    PACKET_PLAY_OUT_WORLD_PARTICLE_1_8(NMSClass.PACKET_PLAY_OUT_WORLD_PARTICLE,
            new Class[] { NMSClass.ENUM_PARTICLE.getClazz(), boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class, int[].class }),

    PACKET_PLAY_OUT_CHAT_1_16(NMSClass.PACKET_PLAY_OUT_CHAT, new Class[] { NMSClass.I_CHAT_BASE_COMPONENT.getClazz(), NMSClass.CHAT_MESSAGE_TYPE.getClazz(), UUID.class }),
    PACKET_PLAY_OUT_CHAT_1_12(NMSClass.PACKET_PLAY_OUT_CHAT, new Class[] { NMSClass.I_CHAT_BASE_COMPONENT.getClazz(), NMSClass.CHAT_MESSAGE_TYPE.getClazz() }),
    PACKET_PLAY_OUT_CHAT_1_8(NMSClass.PACKET_PLAY_OUT_CHAT, new Class[] { NMSClass.I_CHAT_BASE_COMPONENT.getClazz(), byte.class }),

    PACKET_PLAY_OUT_TITLE(NMSClass.PACKET_PLAY_OUT_TITLE, new Class[] { NMSClass.ENUM_TITLE_ACTION.getClazz(), NMSClass.I_CHAT_BASE_COMPONENT.getClazz(), int.class, int.class, int.class });


    private final NMSClass nmsClass;
    private final Class<?>[] arguments;

    private final Constructor<?> constructor;

    NMSConstructor(NMSClass nmsClass, Class<?>[] arguments) {
        this.nmsClass = nmsClass;
        this.arguments = arguments;

        Constructor<?> tempConstructor;

        try {
            tempConstructor = nmsClass.getClazz().getConstructor(this.arguments);
        } catch(NoSuchMethodException exception) {
            tempConstructor = null;
            exception.printStackTrace();
        }

        this.constructor = tempConstructor;
    }

    public NMSClass getNmsClass() { return nmsClass; }
    public Class<?>[] getArguments() { return arguments; }
    public Constructor<?> getConstructor() { return constructor; }
}
