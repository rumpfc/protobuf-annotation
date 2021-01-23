package com.rumpf.exception;

import com.rumpf.proto.PbField;
import com.rumpf.proto.PbFieldType;
import com.rumpf.proto.PbModifier;
import com.rumpf.proto.exception.NotARepeatedFieldException;
import com.rumpf.proto.mapper.PbObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class NotARepeatedFieldTest {

    private static PbObjectMapper mapper;

    @BeforeAll
    public static void setup() {
        mapper = new PbObjectMapper();
    }

    private static class SampleClass {

        @PbField(field = 1, type = PbFieldType.STRING, modifier = PbModifier.REPEATED)
        private String a;
    }

    @Test
    public void duplicateFieldNumberTest() {
        Assertions.assertThrows(
                NotARepeatedFieldException.class,
                () -> mapper.write(new NotARepeatedFieldTest.SampleClass(), ByteBuffer.allocate(1))
        );


    }
}
