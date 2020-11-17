package com.rumpf.proto.field;

import com.rumpf.proto.ProtobufEnum;

import java.lang.reflect.Field;
import java.util.Collection;

public class NotARepeatedFieldException extends RuntimeException {

    private final Field field;

    public NotARepeatedFieldException(Field field) {
        this.field = field;
    }

    @Override
    public String getMessage() {
        return field.toString() + " does not extend " + Collection.class.getName();
    }
}
