package com.example.covid.repository;

import com.example.covid.model.Province;
import javax.persistence.*;

public class ProvinceRepository {

    private EntityManager entityManager;

    public ProvinceRepository() {
        entityManager = Persistence.createEntityManagerFactory("CovidPU").createEntityManager();
    }

    public void save(Province province) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(province);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
    }
}
