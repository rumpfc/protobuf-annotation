package com.rumpf.exception;

import com.rumpf.proto.PbField;
import com.rumpf.proto.PbFieldType;
import com.rumpf.proto.exception.DuplicateFieldNumberException;
import com.rumpf.proto.field.MessageField;
import com.rumpf.proto.mapper.PbObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DuplicateFieldNumberTest {

    private static PbObjectMapper mapper;

    @BeforeAll
    public static void setup() {
        mapper = new PbObjectMapper();
    }

    private static class SampleClass {

        @PbField(field = 3, type = PbFieldType.STRING)
        private String a;

        @PbField(field = 4, type = PbFieldType.STRING)
        private String b;

        @PbField(field = 3, type = PbFieldType.STRING)
        private String c;
    }

    @Test
    public void duplicateFieldNumberTest() {
        DuplicateFieldNumberException ex = Assertions.assertThrows(
                DuplicateFieldNumberException.class,
                () -> mapper.write(new SampleClass(), ByteBuffer.allocate(1))
        );

        Set<String> involvedFields = Stream.of("a", "c").collect(Collectors.toSet());
        MessageField[] fields = ex.getMessageFields();

        Assertions.assertEquals(2, fields.length, "2 Error fields expected");

        Assertions.assertAll(
                () -> Assertions.assertEquals(3, ex.getFieldNumber(), "Expected different field number"),
                () -> Assertions.assertTrue(involvedFields.contains(fields[0].getFieldName())),
                () -> Assertions.assertTrue(involvedFields.contains(fields[1].getFieldName()))
        );
    }
}
