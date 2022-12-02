package com.lielamar.nmsmanager.reflection;

import java.lang.reflect.Field;

public enum NMSFields {

    ENTITY_TYPES_FIELDS(NMSClass.ENTITY_TYPES);


    private final NMSClass nmsClass;

    private final Field[] fields;

    NMSFields(NMSClass nmsClass) {
        this.nmsClass = nmsClass;

        this.fields = nmsClass.getClazz().getFields();
    }

    public Field[] getFields() { return this.fields; }
}
