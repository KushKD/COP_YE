package com.kushkumardhawan.inspections.Model;

import java.io.Serializable;

public class Static implements Serializable {

    public String id;
    public String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Static{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Static() {
    }

    public Static(String id, String value) {
        this.id = id;
        this.value = value;
    }
}
