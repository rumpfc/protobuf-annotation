package com.rumpf.proto;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.rumpf.proto.field.MessageField;
import com.rumpf.proto.field.MessageFieldFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class ProtobufObject {

    private Map<Integer, MessageField> messageFields;

    public ProtobufObject() {
        init();
    }

    private void init() {
        messageFields = Arrays.stream(this.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(PbField.class))
                .map(f -> MessageFieldFactory.newMessageField(this, f))
                .collect(Collectors.toMap(MessageField::getFieldNumber, Function.identity()));
    }

    public void read(byte[] data) throws IOException {
        if(messageFields == null) {
            init();
        }

        CodedInputStream cis = CodedInputStream.newInstance(data);

        while (!cis.isAtEnd()) {
            int tag = cis.readTag();

            if(messageFields.containsKey(tag >> 3)) {
                messageFields.get(tag >> 3).read(cis);
            } else {
                cis.skipField(tag);
            }
        }
    }

    public byte[] write() throws IOException {
        if(messageFields == null) {
            init();
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        CodedOutputStream cos = CodedOutputStream.newInstance(os);

        for(MessageField field : messageFields.values()) {
            field.write(cos);
        }

        cos.flush();
        return os.toByteArray();
    }
}
