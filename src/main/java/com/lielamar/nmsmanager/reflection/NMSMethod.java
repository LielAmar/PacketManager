package com.lielamar.nmsmanager.reflection;

import java.lang.reflect.InvocationTargetException;

public enum NMSMethod {

    CRAFT_PLAYER_GET_HANDLE(NMSClass.CRAFT_PLAYER, "getHandle", null),
    CRAFT_PLAYER_SEND_PACKET(NMSClass.CRAFT_PLAYER, "sendPacket", new Class[] { NMSClass.PACKET.getClazz() }),

    PARTICLE_VALUE_OF(NMSClass.PARTICLE, "value", new Class[] { null }),
    ENUM_PARTICLE_VALUE_OF(NMSClass.ENUM_PARTICLE, "value", new Class[] { null }),

    I_CHAT_BASE_COMPONENT_CHAT_SERIALIZER(NMSClass.I_CHAT_BASE_COMPONENT_SUB_CLASS, "a", new Class[] { String.class }),
    CHAT_MESSAGE_TYPE_CHAT_MESSAGE_VALUE_OF(NMSClass.CHAT_MESSAGE_TYPE, "value", new Class[] { null }),

    ENUM_TITLE_ACTION_VALUE_OF(NMSClass.ENUM_TITLE_ACTION, "value", new Class[] { null }),
    ENTITY_TYPES_A(NMSClass.ENTITY_TYPES, "a", new Class[] { Class.class, String.class, int.class });


    private final NMSClass nmsClass;
    private final String methodName;
    private final Class<?>[] arguments;

    NMSMethod(NMSClass nmsClass, String methodName, Class<?>[] arguments) {
        this.nmsClass = nmsClass;
        this.methodName = methodName;
        this.arguments = arguments;
    }

    public NMSClass getNmsClass() { return nmsClass; }
    public String getMethodName() { return methodName; }
    public Class<?>[] getArguments() { return arguments; }

    public static Object invokeMethod(NMSMethod method, Object instance, Object... arguments) {
        try {
            if(method.getArguments() == null)
                return method.getNmsClass().getClazz().getMethod(method.getMethodName()).invoke(instance);
            else
                return  method.getNmsClass().getClazz().getMethod(method.getMethodName(), method.getArguments()).invoke(instance, arguments);
        } catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public static void setAccessible(NMSMethod method, boolean value) {
        try {
            if(method.getArguments() == null)
                method.getNmsClass().getClazz().getMethod(method.getMethodName()).setAccessible(value);
            else
                method.getNmsClass().getClazz().getMethod(method.getMethodName(), method.getArguments()).setAccessible(value);
        } catch(NoSuchMethodException exception) {
            exception.printStackTrace();
        }
    }
}
