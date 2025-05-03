
package com.example.covid.repository;

import com.example.covid.model.CovidReport;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

public class CovidReportRepository {
    private EntityManager entityManager;

    public CovidReportRepository() {
        entityManager = Persistence.createEntityManagerFactory("covidPU").createEntityManager();

    }

    public void save(CovidReport report) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(report);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
    }

    public List<CovidReport> findByDateAndCountryCode(LocalDate date, String isoCode) {
        TypedQuery<CovidReport> query = entityManager.createQuery(
            "SELECT r FROM CovidReport r WHERE r.date = :date AND r.region.iso = :isoCode",
            CovidReport.class
        );
        query.setParameter("date", date.toString());
        query.setParameter("isoCode", isoCode);
        return query.getResultList();
    }
}
