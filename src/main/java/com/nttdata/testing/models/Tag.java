package com.nttdata.testing.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) //aqui estoy usando jsoninclude para no enviar campos vacios por lo tanto cada peurva mnada solo lo que se necesita si?
public class Tag {
    private Long id;
    private String name;

    public Tag() {
        //igaul para los unhappy paths
    }

    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}