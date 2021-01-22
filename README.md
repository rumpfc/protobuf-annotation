# Protocol Buffer Annotation

Inspired by jackson's fasterxml, a simple library to (de)serialize Protocol Buffer data using annotations.
The usage of Google's protobuf compiler is not necessary.

## Usage

Create an instance of PbObjectMapper and call read/write to read from data/stream or write into one.

```java
public class SampleClass1 {

    @PbField(field = 1, type = PbFieldType.INT32)
    private int id;

    @PbField(field = 2, type = PbFieldType.STRING)
    private String name;

    // Optional Constructor, getter and setter
}
```

### Read from Data (byte array)

```java
try {
    PbObjectMapper mapper = new PbObjectMapper();
    byte[] sampleData = new byte[]{ 
        0x08, 0x14,                                     // ID = 20
        0x12, 0x06, 0x72, 0x75, 0x6D, 0x70, 0x66, 0x63  // name = rumpfc
    };
        
    SampleClass1 sample = mapper.read(sampleData, SampleClass1.class);
    System.out.println(sample);
    // SampleClass1{id=20, name='rumpfc'}
} catch(IOException ex) {
    ex.printStackTrace();
}
```

### Write data (byte array)

```java
try {
    PbObjectMapper mapper = new PbObjectMapper();
    
    SampleClass1 sample = new SampleClass1();
    sample.setId(20);
    sample.setName("rumpfc");
    System.out.println(sample);
    // SampleClass1{id=20, name='rumpfc'}
        
    byte[] output = mapper.write(sample);
    // hex output = [08 14 12 06 72 75 6D 70 66 63]
} catch(IOException ex) {
    ex.printStackTrace();
}
```
