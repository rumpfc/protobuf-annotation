package com.rumpf.proto.exception;

import com.rumpf.proto.field.MessageField;

public class DuplicateFieldNumberException extends RuntimeException {

    private Class<?> clazz;
    private MessageField firstField;
    private MessageField secondField;

    public DuplicateFieldNumberException(Class<?> clazz, MessageField firstField, MessageField secondField) {
        this.clazz = clazz;
        this.firstField = firstField;
        this.secondField = secondField;
    }

    @Override
    public String getMessage() {
        return String.format(
                "Class \"%s\": Field \"%s\" uses the same field number {%d} as Field \"%s\"!",
                clazz.getName(),
                secondField.getFieldName(),
                secondField.getFieldNumber(),
                firstField.getFieldName()
        );
    }

    public Class<?> getObjectClazz() {
        return clazz;
    }

    public MessageField[] getMessageFields() {
        return new MessageField[]{firstField, secondField};
    }

    public int getFieldNumber() {
        return firstField.getFieldNumber();
    }
}
