package com.rumpf.proto.field;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

import java.io.IOException;

public interface MessageField {

    int getFieldNumber();
    String getFieldName();

    void read(CodedInputStream cis) throws IOException;
    void write(CodedOutputStream cos) throws IOException;
}
