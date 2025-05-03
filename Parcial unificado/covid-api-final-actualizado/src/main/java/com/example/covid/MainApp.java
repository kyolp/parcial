package com.example.covid;

import com.example.covid.model.CovidReport;
import com.example.covid.repository.CovidReportRepository;
import com.example.covid.repository.ExecutedReportRepository;
import com.example.covid.service.CovidDataService;

import java.time.LocalDate;

public class MainApp {
    public static void main(String[] args) {
        try {
            System.out.println("Iniciando ejecución...");
            ExecutedReportRepository executedRepo = new ExecutedReportRepository();
            CovidReportRepository reportRepo = new CovidReportRepository();
            CovidDataService service = new CovidDataService(executedRepo, reportRepo);

            service.getReportsByCountryAndDate(LocalDate.parse("2021-01-01"), "GTM")
                   .forEach((provincia, reporte) -> {
                       System.out.println("Provincia: " + provincia);
                       System.out.println("  Confirmados: " + reporte.getConfirmed());
                       System.out.println("  Activos: " + reporte.getActive());
                       System.out.println("  Fallecidos: " + reporte.getDeaths());
   //                    System.out.println("  Región ISO: " + (reporte.getRegion() != null ? reporte.getRegion().getIso() : "Sin región"));
                       System.out.println("------------------------------------------------");
                   });

            System.out.println("Ejecución completada.");
        } catch (Exception e) {
            System.err.println("Error durante la ejecución:");
            e.printStackTrace();
        }
    }
}
