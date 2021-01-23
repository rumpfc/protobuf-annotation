package com.rumpf.proto.field;

import com.rumpf.proto.*;
import com.rumpf.proto.exception.NotARepeatedFieldException;
import com.rumpf.proto.exception.NotCompatibleFieldTypeException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

public class MessageFieldFactory {

    public static MessageField newMessageField(Object pbObject, Field field) {
        PbField f = field.getAnnotation(PbField.class);

        PbFieldType type = f.type();
        PbModifier modifier = f.modifier();
        Class<?> clazz = field.getType();

        if(modifier == PbModifier.REPEATED) {
            if(!Collection.class.isAssignableFrom(clazz)) {
                throw new NotARepeatedFieldException(field);
            } else {
                clazz = ((Class<?>)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0]);
            }
        }

        if(!type.testClass(clazz)) {
            throw new NotCompatibleFieldTypeException(field, type);
        }

        if(modifier == PbModifier.REPEATED) {
            return new RepeatedMessageField(pbObject, field, f.field(), type, modifier);
        } else {
            return new SingleMessageField(pbObject, field, f.field(), type, modifier);
        }
    }
}
