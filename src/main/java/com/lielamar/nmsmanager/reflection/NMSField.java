package com.lielamar.nmsmanager.reflection;

import java.lang.reflect.Field;

public enum NMSField {

    CRAFT_PLAYER_PLAYER_CONNECTION(NMSClass.CRAFT_PLAYER, "playerConnection");


    private final NMSClass nmsClass;
    private final String fieldName;

    private final Field field;

    NMSField(NMSClass nmsClass, String fieldName) {
        this.nmsClass = nmsClass;
        this.fieldName = fieldName;

        Field tempField;

        try {
            tempField = nmsClass.getClazz().getField(fieldName);
        } catch(NoSuchFieldException exception) {
            tempField = null;
            exception.printStackTrace();
        }

        this.field = tempField;
    }

    public Field getField() { return this.field; }
}
