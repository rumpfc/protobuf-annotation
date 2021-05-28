package com.rumpf.proto.field;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.rumpf.proto.*;
import com.rumpf.proto.mapper.PbObjectMapper;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class SingleMessageField extends AbstractMessageField {

    public SingleMessageField(Object object, Field field, int fieldNumber, PbFieldType type, PbModifier modifier) {
        super(object, field, fieldNumber, type, modifier);
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

    public void accept(Object value) {
        switch (type) {
            case ENUM:
                setEnum(value);
                break;
            case MESSAGE:
                setMessage(value);
                break;
            default:
                setPrimitive(value);
        }
    }

    protected Object get() {
        Object value = null;
        try {
            field.setAccessible(true);
            value = field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            field.setAccessible(false);
        }

        return value;
    }

    public void read(CodedInputStream cis) throws IOException {
        accept(type.getReader().apply(cis));
    }

    public void write(CodedOutputStream cos) throws IOException {
        Object value = get();

        if(Objects.nonNull(value)) {
            cos.writeTag(fieldNumber, type.getWireType());
            type.getWriter().accept(value, cos);
        }
    }

    private void setPrimitive(Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
            field.setAccessible(false);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void setEnum(Object value) {
        if(value instanceof Integer) {
            try {
                field.setAccessible(true);

                if(ProtobufEnum.class.isAssignableFrom(field.getType())) {
                    field.set(object, ProtobufEnum.findById(field.getType(), (Integer)value));
                } else {
                    field.set(object, getEnumValues(field.getType())[(Integer)value]);
                }

                field.setAccessible(false);
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    private static Object[] getEnumValues(Class<?> enumClass) throws NoSuchFieldException, IllegalAccessException {
        Field f = enumClass.getDeclaredField("$VALUES");
        f.setAccessible(true);
        Object o = f.get(null);
        return (Object[]) o;
    }

    private void setMessage(Object value) {
        if(value instanceof byte[]) {
            try {
                byte[] data = (byte[]) value;
                PbObjectMapper mapper = new PbObjectMapper();
                Object o = mapper.read(data);

                field.setAccessible(true);
                field.set(this.object, o);
                field.setAccessible(false);

            } catch (IllegalAccessException | IllegalArgumentException | IOException e) {
                e.printStackTrace();
            }
        }
    }

}
