package com.rumpf.mapper;

import com.rumpf.proto.PbField;
import com.rumpf.proto.PbFieldType;
import com.rumpf.proto.ProtobufEnum;
import com.rumpf.proto.mapper.PbObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;

public class EnumMapperTest {

    public enum SampleEnum {
        A, B, C, D, E, F
    }

    public enum SampleEnumWithInterface implements ProtobufEnum {
        A(10),
        B(11),
        C(12),
        D(13),
        E(14),
        F(15);

        SampleEnumWithInterface(int id) {
            this.id = id;
        }

        private final int id;

        @Override
        public int getId() {
            return id;
        }
    }

    public static class SampleClassWithEnum {

        @PbField(field = 1, type = PbFieldType.ENUM)
        private SampleEnum myEnum;

        public SampleEnum getMyEnum() {
            return myEnum;
        }

        public void setMyEnum(SampleEnum myEnum) {
            this.myEnum = myEnum;
        }
    }

    public static class SampleClassWithProtobufEnum {

        @PbField(field = 1, type = PbFieldType.ENUM)
        private SampleEnumWithInterface myEnum;

        public SampleEnumWithInterface getMyEnum() {
            return myEnum;
        }

        public void setMyEnum(SampleEnumWithInterface myEnum) {
            this.myEnum = myEnum;
        }
    }

    @ParameterizedTest
    @EnumSource(SampleEnum.class)
    public void readEnumTest(SampleEnum sampleEnum) throws IOException {

        PbObjectMapper mapper = new PbObjectMapper();
        byte[] bytes = new byte[]{0x08, (byte)sampleEnum.ordinal()};

        SampleClassWithEnum tmp = mapper.read(bytes, SampleClassWithEnum.class);

        Assertions.assertEquals(sampleEnum, tmp.getMyEnum(), "Unexpected enum returned");
    }

    @ParameterizedTest
    @EnumSource(SampleEnumWithInterface.class)
    public void readProtobufEnumTest(SampleEnumWithInterface sampleEnum) throws IOException {

        PbObjectMapper mapper = new PbObjectMapper();
        byte[] bytes = new byte[]{0x08, (byte)sampleEnum.getId()};

        SampleClassWithProtobufEnum tmp = mapper.read(bytes, SampleClassWithProtobufEnum.class);

        Assertions.assertEquals(sampleEnum, tmp.getMyEnum(), "Unexpected enum returned");
    }

    @ParameterizedTest
    @EnumSource(SampleEnum.class)
    public void writeEnumTest(SampleEnum sampleEnum) throws IOException {
        PbObjectMapper mapper = new PbObjectMapper();
        byte[] expectedBytes = new byte[]{0x08, (byte)sampleEnum.ordinal()};

        SampleClassWithEnum tmp = new SampleClassWithEnum();
        tmp.setMyEnum(sampleEnum);

        byte[] result = mapper.write(tmp);
        Assertions.assertArrayEquals(expectedBytes, result, "Unexpected output");
    }

    @ParameterizedTest
    @EnumSource(SampleEnumWithInterface.class)
    public void writeProtobufEnumTest(SampleEnumWithInterface sampleEnum) throws IOException {
        PbObjectMapper mapper = new PbObjectMapper();
        byte[] expectedBytes = new byte[]{0x08, (byte)sampleEnum.getId()};

        SampleClassWithProtobufEnum tmp = new SampleClassWithProtobufEnum();
        tmp.setMyEnum(sampleEnum);

        byte[] result = mapper.write(tmp);
        Assertions.assertArrayEquals(expectedBytes, result, "Unexpected output");
    }
}
