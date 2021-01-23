package com.rumpf.proto.exception;

public class NoPbFieldsException extends RuntimeException {

    private Class<?> clazz;

    public NoPbFieldsException(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getObjectClazz() {
        return clazz;
    }

    @Override
    public String getMessage() {
        return String.format("Class \"%s\" does not contain any fields with @PbField annotation", clazz.getName());
    }
}
