package com.example.covid.model;

import javax.persistence.*;

@Entity
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String iso;

    public Region() {}

    public Region(String name, String iso) {
        this.name = name;
        this.iso = iso;
    }
    public String getIso() {
    return iso;
    }

}
