package com.rumpf.proto.field;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.rumpf.proto.PbFieldType;
import com.rumpf.proto.PbModifier;
import com.rumpf.proto.ProtobufObject;

import java.io.IOException;
import java.lang.reflect.Field;

public abstract class AbstractMessageField implements MessageField {

    protected final Field field;
    protected final int fieldNumber;
    protected final PbModifier modifier;
    protected final PbFieldType type;
    protected final ProtobufObject pbObject;

    protected AbstractMessageField(ProtobufObject pbObject, Field field, int fieldNumber, PbFieldType type, PbModifier modifier) {
        this.field = field;
        this.pbObject = pbObject;
        this.fieldNumber = fieldNumber;
        this.type = type;
        this.modifier = modifier;
    }

    @Override
    public int getFieldNumber() {
        return fieldNumber;
    }

    public abstract void read(CodedInputStream cis) throws IOException;
    public abstract void write(CodedOutputStream cos) throws IOException;
}
