package com.rumpf.exception;

import com.rumpf.proto.exception.NoPbFieldsException;
import com.rumpf.proto.mapper.PbObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class NoPbFieldsTest {

    private static PbObjectMapper mapper;

    @BeforeAll
    public static void setup() {
        mapper = new PbObjectMapper();
    }

    private static class SampleClass1 {

        private String a;
        private String b;
    }

    private static class SampleClass2 {

    }

    @Test
    public void duplicateFieldNumberTest1() {
        NoPbFieldsException ex = Assertions.assertThrows(
                NoPbFieldsException.class,
                () -> mapper.write(new SampleClass1(), ByteBuffer.allocate(1))
        );

        Assertions.assertEquals(SampleClass1.class, ex.getObjectClazz(), "Unexpected Class<?> object returned");
    }

    @Test
    public void duplicateFieldNumberTest2() {
        NoPbFieldsException ex = Assertions.assertThrows(
                NoPbFieldsException.class,
                () -> mapper.write(new SampleClass2(), ByteBuffer.allocate(1))
        );

        Assertions.assertEquals(SampleClass2.class, ex.getObjectClazz(), "Unexpected Class<?> object returned");
    }
}
