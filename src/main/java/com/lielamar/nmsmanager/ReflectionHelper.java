package com.lielamar.nmsmanager;

import com.lielamar.nmsmanager.modules.Version;

import java.lang.reflect.Field;

public class ReflectionHelper {

    /**
     * @param className   Name of the class
     * @return            A class object of form: "net.minecraft.server.server_version.class"
     */
    public static Class<?> getNMSClass(String className) {
        return getServerVersionClass("net.minecraft.server", className);
    }

    /**
     * Returns a class with consideration of the server version
     *
     * @param packageName   Name of the package
     * @param className     Name of the class
     * @return              A class object of form: "package.server_version.class"
     */
    public static Class<?> getServerVersionClass(String packageName, String className) {
        return getClass(packageName + "." + Version.getInstance().getNMSVersion().getVersionName(), className);
    }

    /**
     * Returns a class by package & class name
     *
     * @param packageName   Name of the package
     * @param className     Name of the class
     * @return              A class object of form: "package.class"
     */
    public static Class<?> getClass(String packageName, String className) {
        String name = packageName + "." + className;
        Class<?> nmsClass = null;

        try { nmsClass = Class.forName(name); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }

        return nmsClass;
    }

    /**
     * Returns a private field of clazz
     *
     * @param fieldName   Private field to get
     * @param clazz       Class to get field of
     * @param object      Object to use in order to get the field
     * @return            Private field
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
     * Checks if clazz is inheriting from superClass
     *
     * @param clazz        Class to check if inherits from superClass
     * @param superClass   The super class
     * @return             Whether clazz inherits from superClass
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
