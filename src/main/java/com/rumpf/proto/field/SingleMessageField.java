package com.rumpf.proto.field;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.rumpf.proto.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class SingleMessageField extends AbstractMessageField {

    public SingleMessageField(ProtobufObject pbObject, Field field, int fieldNumber, PbFieldType type, PbModifier modifier) {
        super(pbObject, field, fieldNumber, type, modifier);
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
            value = field.get(pbObject);
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
            field.set(pbObject, value);
            field.setAccessible(false);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void setEnum(Object value) {
        if(value instanceof Integer) {
            try {
                field.setAccessible(true);
                field.set(pbObject, ProtobufEnum.findById(field.getType(), (Integer)value));
                field.setAccessible(false);
            } catch (IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    private void setMessage(Object value) {
        if(value instanceof byte[]) {
            try {
                byte[] data = (byte[]) value;
                Constructor<?> constructor = field.getType().getConstructor();
                Object o = constructor.newInstance();
                if(o instanceof ProtobufObject) {
                    ((ProtobufObject)o).read(data);
                    field.setAccessible(true);
                    field.set(pbObject, o);
                    field.setAccessible(false);
                }
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException | InstantiationException | IOException e) {
                e.printStackTrace();
            }
        }
    }

}
