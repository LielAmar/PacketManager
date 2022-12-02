package com.lielamar.nmsmanager.registry;

import com.lielamar.nmsmanager.ReflectionHelper;
import com.lielamar.nmsmanager.modules.Version;
import com.lielamar.nmsmanager.reflection.NMSClass;
import com.lielamar.nmsmanager.reflection.NMSFields;
import com.lielamar.nmsmanager.reflection.NMSMethod;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityRegistry extends Registry {

    private final String name;
    private final int id;
    private final Class<?> nmsClass;
    private final Class<?> customClass;

    private EntityRegistry(String name, int id, Class<?> nmsClass, Class<?> customClass) {
        this.name = name;
        this.id = id;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
    }

    public static class Builder {

        private String name;
        private int id;
        private Class<?> nmsClass;
        private Class<?> customClass;

        public Builder() {
            this.name = null;
            this.id = -1;
            this.nmsClass = null;
            this.customClass = null;
        }

        public Builder(Builder builder) {
            this.name = builder.name;
            this.id = builder.id;
            this.nmsClass = builder.nmsClass;
            this.customClass = builder.customClass;
        }

        public Builder(String name, int id, Class<?> nmsClass, Class<?> customClass) {
            this.name = name;
            this.id = id;
            this.nmsClass = nmsClass;
            this.customClass = customClass;
        }


        public Builder setName(String name) {
            this.name = name;

            return this;
        }

        public Builder setId(int id) {
            this.id = id;

            return this;
        }

        public Builder setNmsClass(Class<?> nmsClass) {
            this.nmsClass = nmsClass;

            return this;
        }

        public Builder setCustomClass(Class<?> customClass) {
            this.customClass = customClass;

            return this;
        }


        public EntityRegistry build() {
            return new EntityRegistry(this.name, this.id, this.nmsClass, this.customClass);
        }
    }


    @Override
    public void register() {
        if(!shouldRegistration()) return;

        try {
            List<Map<?, ?>> dataMap = getDataMap();

            if(dataMap.get(2).containsKey(id)) {
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }

            NMSMethod.setAccessible(NMSMethod.ENTITY_TYPES_A, true);
            NMSMethod.invokeMethod(NMSMethod.ENTITY_TYPES_A, null, customClass, name, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unregister() {
        if(!shouldRegistration()) return;

        try {
            List<Map<?, ?>> dataMap = getDataMap();

            if(dataMap.get(2).containsKey(id)) {
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean shouldRegistration() {
        // No need to register entities after 1.12
        if(Version.getInstance().getNMSVersion().above(Version.NMSVersion.v1_12_R1)) return false;

        // If the class doesn't inherit from EntityInsentient we don't want to continue with registering/unregistering
        return ReflectionHelper.isInheriting(nmsClass, NMSClass.ENTITY_INSENTIENT.getClazz()) && ReflectionHelper.isInheriting(customClass, NMSClass.ENTITY_INSENTIENT.getClazz());
    }

    private List<Map<?, ?>> getDataMap() throws IllegalAccessException {
        List<Map<?, ?>> dataMap = new ArrayList<>();
        for(Field f : NMSFields.ENTITY_TYPES_FIELDS.getFields()) {
            if(f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                f.setAccessible(true);
                dataMap.add((Map<?, ?>) f.get(null));
            }
        }

        return dataMap;
    }
}
