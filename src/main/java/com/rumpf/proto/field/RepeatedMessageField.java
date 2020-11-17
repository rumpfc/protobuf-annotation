package com.rumpf.proto.field;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.rumpf.proto.PbFieldType;
import com.rumpf.proto.PbModifier;
import com.rumpf.proto.ProtobufEnum;
import com.rumpf.proto.ProtobufObject;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class RepeatedMessageField extends SingleMessageField {

    private Collection coll;

    public RepeatedMessageField(ProtobufObject pbObject, Field field, int fieldNumber, PbFieldType type, PbModifier modifier) {
        super(pbObject, field, fieldNumber, type, modifier);
    }

    private void initCollection() {

        Class<?> clazz = field.getType();
        if(!Collection.class.isAssignableFrom(clazz)) {
            return;
        }

        coll = (Collection)get();

        if(Objects.nonNull(coll)) {
            return;
        }

        if(clazz.isInterface()) {
            if(List.class.isAssignableFrom(clazz)) {
                coll = new ArrayList<>();
            } else if(Set.class.isAssignableFrom(clazz)) {
                coll = new HashSet<>();
            } else if(Queue.class.isAssignableFrom(clazz)) {
                coll = new ArrayDeque<>();
            } else if(Deque.class.isAssignableFrom(clazz)) {
                coll = new ArrayDeque<>();
            } else {
                // default
                coll = new ArrayList<>();
            }
        } else {
            try {
                coll = (Collection) clazz.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }


        try {
            field.setAccessible(true);
            field.set(pbObject, coll);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            field.setAccessible(false);
        }

    }

    @Override
    public void accept(Object value) {
        if(Objects.isNull(coll)) {
            initCollection();
        }

        switch (type) {
            case ENUM:
                coll.add(ProtobufEnum.findById(field.getType(), (Integer)value));
                break;
            case MESSAGE:
                coll.add(toPbObject(value));
                break;
            default:
                coll.add(value);
        }
    }

    @Override
    public void write(CodedOutputStream cos) throws IOException {
        if(Objects.isNull(coll)) {
            initCollection();
        }

        for(Object o : coll) {
            if(Objects.nonNull(o)) {
                cos.writeTag(fieldNumber, type.getWireType());
                type.getWriter().accept(o, cos);
            }
        }

    }

    private ProtobufObject toPbObject(Object value) {
        if(value instanceof byte[]) {
            try {
                byte[] data = (byte[]) value;
                Constructor<?> constructor = getCollectionGenericClass().getConstructor();
                Object o = constructor.newInstance();
                if(o instanceof ProtobufObject) {
                    ((ProtobufObject)o).read(data);
                    return (ProtobufObject)o;
                }
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException | InstantiationException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Class<?> getCollectionGenericClass() {
        return (Class<?>)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
    }
}
