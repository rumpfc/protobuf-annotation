package com.rumpf.proto;

import com.google.protobuf.CodedInputStream;

import java.util.function.Predicate;

public enum PbFieldType {
    BOOL(0, c -> boolean.class.isAssignableFrom(c) || Boolean.class.isAssignableFrom(c), CodedInputStream::readBool, (v, c) -> c.writeBoolNoTag((Boolean)v)),
    INT32(0, c -> int.class.isAssignableFrom(c) || Integer.class.isAssignableFrom(c), CodedInputStream::readInt32, (v, c) -> c.writeInt32NoTag((Integer)v)),
    UINT32(0, c -> int.class.isAssignableFrom(c) || Integer.class.isAssignableFrom(c), CodedInputStream::readUInt32, (v, c) -> c.writeUInt32NoTag((Integer)v)),
    SINT32(0, c -> int.class.isAssignableFrom(c) || Integer.class.isAssignableFrom(c), CodedInputStream::readSInt32, (v, c) -> c.writeSInt32NoTag((Integer)v)),
    INT64(0, c -> long.class.isAssignableFrom(c) || Long.class.isAssignableFrom(c), CodedInputStream::readInt64, (v, c) -> c.writeInt64NoTag((Long)v)),
    UINT64(0, c -> long.class.isAssignableFrom(c) || Long.class.isAssignableFrom(c), CodedInputStream::readUInt64, (v, c) -> c.writeUInt64NoTag((Long)v)),
    SINT64(0, c -> long.class.isAssignableFrom(c) || Long.class.isAssignableFrom(c), CodedInputStream::readSInt64, (v, c) -> c.writeSInt64NoTag((Long)v)),
    ENUM(0, ProtobufEnum.class::isAssignableFrom, CodedInputStream::readEnum, (v, c) -> c.writeEnumNoTag(((ProtobufEnum)v).getId())),
    FIXED64(1, c -> long.class.isAssignableFrom(c) || Long.class.isAssignableFrom(c), CodedInputStream::readFixed64, (v, c) -> c.writeFixed64NoTag((Long)v)),
    SFIXED64(1, c -> long.class.isAssignableFrom(c) || Long.class.isAssignableFrom(c), CodedInputStream::readSFixed64, (v, c) -> c.writeSFixed64NoTag((Long)v)),
    DOUBLE(1, c -> double.class.isAssignableFrom(c) || Double.class.isAssignableFrom(c), CodedInputStream::readDouble, (v, c) -> c.writeDoubleNoTag((Double)v)),
    STRING(2, String.class::isAssignableFrom, CodedInputStream::readString, (v, c) -> c.writeStringNoTag((String)v)),
    BYTES(2, byte[].class::isAssignableFrom, CodedInputStream::readByteArray, (v, c) -> c.writeByteArrayNoTag((byte[])v)),
    MESSAGE(2, ProtobufObject.class::isAssignableFrom, CodedInputStream::readByteArray, (v, c) -> c.writeByteArrayNoTag(((ProtobufObject)v).write())),
    FIXED32(5, c -> int.class.isAssignableFrom(c) || Integer.class.isAssignableFrom(c), CodedInputStream::readFixed32, (v, c) -> c.writeFixed32NoTag((Integer)v)),
    SFIXED32(5, c -> int.class.isAssignableFrom(c) || Integer.class.isAssignableFrom(c), CodedInputStream::readSFixed32, (v, c) -> c.writeSFixed32NoTag((Integer)v)),
    FLOAT(5, c -> float.class.isAssignableFrom(c) || Float.class.isAssignableFrom(c), CodedInputStream::readFloat, (v, c) -> c.writeFloatNoTag((Float)v))
    ;

    PbFieldType(int wireType, Predicate<Class<?>> clazzTest, PbFieldReader reader, PbFieldWriter writer) {
        this.wireType = wireType;
        this.clazzTest = clazzTest;
        this.reader = reader;
        this.writer = writer;
    }

    private final int wireType;
    private final Predicate<Class<?>> clazzTest;
    private final PbFieldReader reader;
    private final PbFieldWriter writer;

    public int getWireType() {
        return wireType;
    }

    public boolean testClass(Class<?> clazz) {
        return clazzTest.test(clazz);
    }

    public PbFieldReader getReader() {
        return reader;
    }

    public PbFieldWriter getWriter() {
        return writer;
    }
}
