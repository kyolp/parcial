
package com.example.covid.repository;

import com.example.covid.model.ExecutedReport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.time.LocalDate;
public class ExecutedReportRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean existsByDateAndCountry(LocalDate date, String countryIso) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(e) FROM ExecutedReport e WHERE e.executionDate = :date AND e.countryIso = :country",
            Long.class
        );
        query.setParameter("date", date);
        query.setParameter("country", countryIso);
        return query.getSingleResult() > 0;
    }

    public void save(ExecutedReport report) {
        entityManager.getTransaction().begin();
        entityManager.persist(report);
        entityManager.getTransaction().commit();
    }
}
