package com.rumpf.proto.mapper;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.rumpf.proto.PbField;
import com.rumpf.proto.field.MessageField;
import com.rumpf.proto.field.MessageFieldFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PbObjectMapper {

    private final Map<Integer, MessageField> messageFields;

    public PbObjectMapper() {
        messageFields = new HashMap<>();
    }

    private <T> T createInstance(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getConstructor();
            constructor.setAccessible(true);

            return constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void initMessageFields(Object object) {
        messageFields.clear();

        Arrays.stream(object.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(PbField.class))
                .map(f -> MessageFieldFactory.newMessageField(object, f))
                .forEach(mf -> messageFields.put(mf.getFieldNumber(), mf));
    }

    public byte[] write(Object object) throws IOException {
        initMessageFields(object);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        CodedOutputStream cos = CodedOutputStream.newInstance(os);

        for(MessageField field : messageFields.values()) {
            field.write(cos);
        }

        cos.flush();
        return os.toByteArray();
    }

    public Object read(byte[] data) throws IOException {
        return read(data, Object.class);
    }

    public <T> T read(byte[] data, Class<T> clazz) throws IOException {
        T object = createInstance(clazz);

        if(object == null) {
            return null;
        }

        initMessageFields(object);

        CodedInputStream cis = CodedInputStream.newInstance(data);

        while (!cis.isAtEnd()) {
            int tag = cis.readTag();

            if(messageFields.containsKey(tag >> 3)) {
                messageFields.get(tag >> 3).read(cis);
            } else {
                cis.skipField(tag);
            }
        }

        return object;
    }
}
