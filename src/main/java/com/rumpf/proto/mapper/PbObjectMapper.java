package com.rumpf.proto.mapper;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.rumpf.proto.PbField;
import com.rumpf.proto.field.MessageField;
import com.rumpf.proto.field.MessageFieldFactory;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
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

    ////////////////////////////////////////////////////
    /////             write operations             /////
    ////////////////////////////////////////////////////

    public byte[] write(Object object) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        CodedOutputStream cos = CodedOutputStream.newInstance(os);

        write(object, cos);

        return os.toByteArray();
    }

    public void write(Object object, byte[] flatArray) throws IOException {
        write(object, CodedOutputStream.newInstance(flatArray));
    }

    public void write(Object object, byte[] flatArray, int offset, int length) throws IOException {
        write(object, CodedOutputStream.newInstance(flatArray, offset, length));
    }

    public void write(Object object, ByteBuffer buffer) throws IOException {
        write(object, CodedOutputStream.newInstance(buffer));
    }

    public void write(Object object, File file) throws IOException {
        write(object, new FileOutputStream(file));
    }

    public void write(Object object, OutputStream output) throws IOException {
        write(object, CodedOutputStream.newInstance(output));
    }

    public void write(Object object, OutputStream output, int bufferSize) throws IOException {
        write(object, CodedOutputStream.newInstance(output, bufferSize));
    }

    private void write(Object object, CodedOutputStream cos) throws IOException {
        initMessageFields(object);

        for(MessageField field : messageFields.values()) {
            field.write(cos);
        }

        cos.flush();
    }

    ///////////////////////////////////////////////////
    /////             read operations             /////
    ///////////////////////////////////////////////////

    public Object read(byte[] data) throws IOException {
        return read(data, Object.class);
    }

    public <T> T read(byte[] data, Class<T> clazz) throws IOException {
        return read(CodedInputStream.newInstance(data), clazz);
    }

    public Object read(byte[] data, int off, int len) throws IOException {
        return read(data, off, len, Object.class);
    }

    public <T> T read(byte[] data, int off, int len, Class<T> clazz) throws IOException {
        return read(CodedInputStream.newInstance(data, off, len), clazz);
    }

    public Object read(ByteBuffer buf) throws IOException {
        return read(buf, Object.class);
    }

    public <T> T read(ByteBuffer buf, Class<T> clazz) throws IOException {
        return read(CodedInputStream.newInstance(buf), clazz);
    }

    public Object read(File source) throws IOException {
        return read(source, Object.class);
    }

    public <T> T read(File source, Class<T> clazz) throws IOException {
        return read(new FileInputStream(source), clazz);
    }

    public Object read(InputStream input) throws IOException {
        return read(input, Object.class);
    }

    public <T> T read(InputStream input, Class<T> clazz) throws IOException {
        return read(CodedInputStream.newInstance(input), clazz);
    }

    public Object read(InputStream input, int buffersize) throws IOException {
        return read(input, buffersize, Object.class);
    }

    public <T> T read(InputStream input, int buffersize, Class<T> clazz) throws IOException {
        return read(CodedInputStream.newInstance(input, buffersize), clazz);
    }

    public Object read(Iterable<ByteBuffer> input) throws IOException {
        return read(input, Object.class);
    }

    public <T> T read(Iterable<ByteBuffer> input, Class<T> clazz) throws IOException {
        return read(CodedInputStream.newInstance(input), clazz);
    }

    private <T> T read(CodedInputStream cis, Class<T> clazz) throws IOException {
        T object = createInstance(clazz);

        if(object == null) {
            return null;
        }

        initMessageFields(object);

        while (!cis.isAtEnd()) {
            int tag = cis.readTag();
            int fieldNumber = tag >> 3;

            if(messageFields.containsKey(fieldNumber)) {
                messageFields.get(fieldNumber).read(cis);
            } else {
                cis.skipField(tag);
            }
        }

        return object;
    }
}
