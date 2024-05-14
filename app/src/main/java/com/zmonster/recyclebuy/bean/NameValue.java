package com.zmonster.recyclebuy.bean;

import java.util.ArrayList;
import java.util.List;

public class NameValue {
    private Long id;
    public String name;
    public Object value;

    public NameValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public static List<NameValue> list() {
        return new ArrayList<NameValue>();
    }

    @Override
    public String toString() {
        return name + " : " + (value != null ? value.toString() : "null");
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
