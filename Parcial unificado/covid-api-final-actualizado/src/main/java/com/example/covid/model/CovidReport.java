package com.example.covid.model;

import javax.persistence.*;

@Entity
@Table(name = "covidreport")
public class CovidReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;

    private String province;

    private int confirmed;
    private int deaths;
    private int recovered;
    private int active;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String rawDataJson;

    // 🔹 Constructor vacío requerido por JPA
    public CovidReport() {}

    // 🔹 Getters
    public Long getId() { return id; }

    public String getDate() { return date; }

    public String getProvince() { return province; }

    public int getConfirmed() { return confirmed; }

    public int getDeaths() { return deaths; }

    public int getRecovered() { return recovered; }

    public int getActive() { return active; }

    public String getRawDataJson() { return rawDataJson; }

    // 🔹 Setters
    public void setDate(String date) {
        this.date = date;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public void setRawDataJson(String rawDataJson) {
        this.rawDataJson = rawDataJson;
    }

    // 🔹 toString útil para depuración
    @Override
    public String toString() {
        return "CovidReport{" +
                "province='" + province + '\'' +
                ", confirmed=" + confirmed +
                ", deaths=" + deaths +
                ", recovered=" + recovered +
                ", active=" + active +
                '}';
    }
    
}