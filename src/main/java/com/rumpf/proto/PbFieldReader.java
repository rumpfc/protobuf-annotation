package com.rumpf.proto;

import com.google.protobuf.CodedInputStream;

import java.io.IOException;

@FunctionalInterface
public interface PbFieldReader {

    Object apply(CodedInputStream cis) throws IOException;
}
