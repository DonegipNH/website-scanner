package com.vts.websitescanner.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MetaTag {
    @JsonProperty("name")
    private String name;

    @JsonProperty("property")
    private String property;

    public MetaTag() {
    }

    public MetaTag(String name, String property) {
        this.name = name;
        this.property = property;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return "MetaTag{" +
                "name='" + name + '\'' +
                ", property='" + property + '\'' +
                '}';
    }
}