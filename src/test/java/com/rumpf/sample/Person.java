package com.rumpf.sample;

import com.rumpf.proto.PbField;
import com.rumpf.proto.PbFieldType;
import com.rumpf.proto.PbModifier;
import com.rumpf.proto.ProtobufEnum;

import java.util.ArrayList;
import java.util.Collection;

public class Person {

    @PbField(field = 1, type = PbFieldType.STRING, modifier = PbModifier.REQUIRED)
    private String name;

    @PbField(field = 2, type = PbFieldType.INT32, modifier = PbModifier.REQUIRED)
    private int id;

    @PbField(field = 3, type = PbFieldType.STRING, modifier = PbModifier.OPTIONAL)
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<PhoneNumber> getPhones() {
        return phones;
    }

    public void setPhones(Collection<PhoneNumber> phones) {
        this.phones = phones;
    }

    public enum PhoneType {
        MOBILE,
        HOME,
        WORK
    }

    public static class PhoneNumber {

        @PbField(field = 1, type = PbFieldType.STRING, modifier = PbModifier.REQUIRED)
        private String number;

        @PbField(field = 2, type = PbFieldType.ENUM, modifier = PbModifier.OPTIONAL)
        private PhoneType type = PhoneType.HOME;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public PhoneType getType() {
            return type;
        }

        public void setType(PhoneType type) {
            this.type = type;
        }
    }

    @PbField(field = 4, type = PbFieldType.MESSAGE, modifier = PbModifier.REPEATED)
    private Collection<PhoneNumber> phones = new ArrayList<>();

    public void addPhoneNumber(String number, PhoneType type) {
        PhoneNumber pn = new PhoneNumber();
        pn.setNumber(number);
        pn.setType(type);
        phones.add(pn);
    }
}
