package com.visage.cloud.api.domain.entity;

import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

//import javax.persistence.Entity;

//@Document(indexName = "vectors")
//@Entity
public class Vector {

    @Id
    private String id;

    @Field(
            type = FieldType.Double,
            store = true
    )
    private double c1;

    @Field(
            type = FieldType.Double,
            store = true
    )
    double c2;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getC1() {
        return c1;
    }

    public void setC1(double c1) {
        this.c1 = c1;
    }

    public double getC2() {
        return c2;
    }

    public void setC2(double c2) {
        this.c2 = c2;
    }
}
