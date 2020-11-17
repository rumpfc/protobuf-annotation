package com.rumpf.proto;

import com.google.protobuf.CodedOutputStream;

import java.io.IOException;

@FunctionalInterface
public interface PbFieldWriter {

    void accept(Object value, CodedOutputStream cos) throws IOException;
}
