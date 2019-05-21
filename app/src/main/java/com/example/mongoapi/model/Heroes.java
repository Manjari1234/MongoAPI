package com.example.mongoapi.model;

public class Heroes {

    private String id;
    private  String name;
    private String desc;

    public Heroes(String id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public Heroes(String name, String desc) {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
