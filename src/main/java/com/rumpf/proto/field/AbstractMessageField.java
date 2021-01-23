package com.rumpf.proto.field;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.rumpf.proto.PbFieldType;
import com.rumpf.proto.PbModifier;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;

public abstract class AbstractMessageField implements MessageField {

    protected final Field field;
    protected final int fieldNumber;
    protected final PbModifier modifier;
    protected final PbFieldType type;
    protected final Object object;

    protected AbstractMessageField(Object object, Field field, int fieldNumber, PbFieldType type, PbModifier modifier) {
        this.field = field;
        this.object = object;
        this.fieldNumber = fieldNumber;
        this.type = type;
        this.modifier = modifier;
    }

    @Override
    public int getFieldNumber() {
        return fieldNumber;
    }

    @Override
    public String getFieldName() {
        return Objects.nonNull(field) ? field.getName() : null;
    }

    public abstract void read(CodedInputStream cis) throws IOException;
    public abstract void write(CodedOutputStream cos) throws IOException;
}
