package com.rumpf.proto.field;

import com.rumpf.proto.PbFieldType;
import com.rumpf.proto.ProtobufEnum;

import java.lang.reflect.Field;

public class NotCompatibleFieldTypeException extends RuntimeException {

    private final Field field;
    private final PbFieldType type;

    public NotCompatibleFieldTypeException(Field field, PbFieldType type) {
        this.field = field;
        this.type = type;
    }

    @Override
    public String getMessage() {
        String msg = field.toString() + " is not compatible with PbFieldType " + type + ": expected ";

        switch (type) {
            case BOOL:
                msg += boolean.class.getName() + " or " + Boolean.class.getName();
                break;
            case INT32:
            case UINT32:
            case SINT32:
            case FIXED32:
            case SFIXED32:
                msg += int.class.getName() + " or " + Integer.class.getName();
                break;
            case INT64:
            case SINT64:
            case UINT64:
            case FIXED64:
            case SFIXED64:
                msg += long.class.getName() + " or " + Long.class.getName();
                break;
            case FLOAT:
                msg += float.class.getName() + " or " + Float.class.getName();
                break;
            case DOUBLE:
                msg += double.class.getName() + " or " + Double.class.getName();
                break;
            case STRING:
                msg += String.class.getName();
                break;
            case BYTES:
                msg += byte[].class.getName();
                break;
            case MESSAGE:
                msg += "class";
                break;
            case ENUM:
                msg += "enum which implements " + ProtobufEnum.class.getName();
                break;
        }

        return msg;
    }
}
