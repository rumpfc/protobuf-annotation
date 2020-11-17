package com.rumpf.proto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PbField {

    int field();

    PbFieldType type();

    PbModifier modifier() default PbModifier.REQUIRED;
}
