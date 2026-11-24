package com.nttdata.testing.models;

import java.util.Collections;
import java.util.List;

/**
 * Modelo de datos para representar una Mascota (Pet).
 * Centraliza la estructura y construccion del body JSON para evitar duplicacion de codigo.
 */
public class Pet {
    private Long id;
    private String name;
    private List<String> photoUrls;
    private String status;

    public Pet() {
    }

    public Pet(Long id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.photoUrls = Collections.singletonList("https://example.com/mascota.png");
    }

    public static Pet from(String id, String name, String status) {
        Long petId = null;
        if (id != null && !id.trim().isEmpty()) {
            petId = Long.parseLong(id.trim());
        }
        return new Pet(petId, name, status);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
