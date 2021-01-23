package com.rumpf.mapper;

import com.rumpf.proto.PbField;
import com.rumpf.proto.PbFieldType;
import com.rumpf.proto.mapper.PbObjectMapper;
import com.rumpf.sample.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class PbObjectMapperTest {

    private static class SampleClass1 {

        @PbField(field = 1, type = PbFieldType.INT32)
        private int id;

        @PbField(field = 2, type = PbFieldType.STRING)
        private String name;

        public SampleClass1() {
            id = -1;
            name = null;
        }

        public SampleClass1(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "SampleClass1{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    private PbObjectMapper mapper;

    @BeforeEach
    public void setup() {
        mapper = new PbObjectMapper();
    }

    @Test
    public void objectMapperWriteTest() throws Exception {
        SampleClass1 sample = new SampleClass1(20, "rumpfc");
        byte[] expected = new byte[]{ 0x08, 0x14, 0x12, 0x06, 0x72, 0x75, 0x6D, 0x70, 0x66, 0x63};

        byte[] result = mapper.write(sample);

        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    public void objectMapperReadTest() throws Exception {
        byte[] sampleData = new byte[]{ 0x08, 0x14, 0x12, 0x06, 0x72, 0x75, 0x6D, 0x70, 0x66, 0x63};

        SampleClass1 sampleObject = mapper.read(sampleData, SampleClass1.class);

        System.out.println(sampleObject);

        Assertions.assertAll(
                () -> Assertions.assertEquals(20, sampleObject.getId()),
                () -> Assertions.assertEquals("rumpfc", sampleObject.getName())
        );
    }

    @Test
    public void writeObjectWithObjects() throws Exception {
        Person person = new Person();

        person.setId(19465);
        person.setName("Christian Rumpf");
        person.setEmail("christian.rumpf88@gmx.at");

        person.addPhoneNumber("+433142.....", Person.PhoneType.WORK);
        person.addPhoneNumber("+43664.......", Person.PhoneType.MOBILE);

        byte[] expected = new byte[84];
        ByteBuffer buffer = ByteBuffer.wrap(expected);

        buffer.put((byte)0x0A); // tag
        buffer.put((byte)0x0F); // length
        buffer.put("Christian Rumpf".getBytes(StandardCharsets.UTF_8));

        buffer.put((byte) 0x10); // tag
        buffer.put((byte) 0x89);
        buffer.put((byte) 0x98);
        buffer.put((byte) 0x01);

        buffer.put((byte) 0x1A); // tag
        buffer.put((byte) 0x18); // length
        buffer.put("christian.rumpf88@gmx.at".getBytes(StandardCharsets.UTF_8));

        buffer.put((byte) 0x22); // tag
        buffer.put((byte) 0x10); // length
        buffer.put((byte) 0x0A); // tag
        buffer.put((byte) 0x0C); // length
        buffer.put("+433142.....".getBytes(StandardCharsets.UTF_8));
        buffer.put((byte) 0x10); // tag
        buffer.put((byte) 0x02); // WORK


        buffer.put((byte) 0x22); // tag
        buffer.put((byte) 0x11); // length
        buffer.put((byte) 0x0A); // tag
        buffer.put((byte) 0x0D); // length
        buffer.put("+43664.......".getBytes(StandardCharsets.UTF_8));
        buffer.put((byte) 0x10); // tag
        buffer.put((byte) 0x00); // MOBILE

        byte[] result = mapper.write(person);

        Assertions.assertArrayEquals(expected, result);
    }
}
