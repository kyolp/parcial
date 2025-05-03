package com.example.covid.model;

import javax.persistence.*;

@Entity
public class Province {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String iso;

    public Province() {}

    public Province(String name, String iso) {
        this.name = name;
        this.iso = iso;
    }
}
