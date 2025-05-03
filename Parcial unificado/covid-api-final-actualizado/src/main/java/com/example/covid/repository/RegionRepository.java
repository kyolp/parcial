package com.example.covid.repository;

import com.example.covid.model.Region;
import javax.persistence.*;

public class RegionRepository {

    private EntityManager entityManager;

    public RegionRepository() {
        entityManager = Persistence.createEntityManagerFactory("CovidPU").createEntityManager();
    }

    public void save(Region region) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(region);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
    }
}
