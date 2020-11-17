package com.rumpf.proto;

import java.util.Arrays;

public interface ProtobufEnum {

    int getId();

    static ProtobufEnum findById(Class<?> enumClass, int id) {
        if(!(enumClass.isEnum() && ProtobufEnum.class.isAssignableFrom(enumClass))) {
            return null;
        }

        return Arrays.stream(enumClass.getEnumConstants())
                .map(ProtobufEnum.class::cast)
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
