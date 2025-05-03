package com.example.covid.service;

import com.example.covid.model.CovidReport;
import com.example.covid.model.ExecutedReport;
import com.example.covid.repository.CovidReportRepository;
import com.example.covid.repository.ExecutedReportRepository;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.*;
import java.time.LocalDate;

public class CovidDataService implements Runnable {

    private static final Logger logger = Logger.getLogger(CovidDataService.class);
    private final ExecutedReportRepository executedReportRepository;
    private final CovidReportRepository covidReportRepository;

    public CovidDataService(ExecutedReportRepository executedReportRepository,
                            CovidReportRepository covidReportRepository) {
        this.executedReportRepository = executedReportRepository;
        this.covidReportRepository = covidReportRepository;
    }

    @Override
    public void run() {
        try {
            Properties props = new Properties();
            InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties");
            props.load(input);

            LocalDate reportDate = LocalDate.parse(props.getProperty("covid.report.date"));

            List<String> countryIsoList = List.of("USA", "GTN"); // Simulación

            for (String iso : countryIsoList) {
                if (executedReportRepository.existsByDateAndCountry(reportDate, iso)) {
                    logger.info("El país " + iso + " ya fue procesado en la fecha " + reportDate);
                    continue;
                }

                // Aquí debería ir el flujo de procesamiento real
                logger.info("Procesando datos para " + iso + " en la fecha " + reportDate);
                // procesar(iso, reportDate); // Simulación

                ExecutedReport report = new ExecutedReport(reportDate, iso);
                executedReportRepository.save(report);
                logger.info("País " + iso + " procesado y guardado correctamente.");
            }

        } catch (IOException e) {
            logger.error("Error leyendo application.properties: " + e.getMessage());
        }
    }

    public Map<String, CovidReport> getReportsByCountryAndDate(LocalDate date, String isoCode) {
        List<CovidReport> reports = covidReportRepository.findByDateAndCountryCode(date, isoCode);
        Map<String, CovidReport> uniqueReports = new TreeMap<>();

        for (CovidReport report : reports) {
            String key = report.getProvince(); // Asegúrate que getProvince() existe
            if (key != null && !key.isEmpty()) {
                uniqueReports.put(key, report);
            }
        }

        uniqueReports.forEach((province, report) -> {
            System.out.println("Provincia: " + province + " -> " + report);
        });

        return uniqueReports;
    }
    
    
    private String fetchDataFromApi(String iso, LocalDate date) throws IOException {
    String apiUrl = "https://covid-19-statistics.p.rapidapi.com/reports?date=" + date + "&iso=" + iso;
    HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();

    connection.setRequestMethod("GET");
    connection.setRequestProperty("x-rapidapi-key", "2505eda46amshc60713983b5e807p1da25ajsn36febcbf4a71");
    connection.setRequestProperty("x-rapidapi-host", "covid-19-statistics.p.rapidapi.com");

    int responseCode = connection.getResponseCode();
    if (responseCode != 200) {
        throw new IOException("Error al conectar con la API: " + responseCode);
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    StringBuilder response = new StringBuilder();
    String line;

    while ((line = reader.readLine()) != null) {
        response.append(line);
    }
    reader.close();

    return response.toString();
}
    private void saveCovidDataFromApi(String iso, LocalDate date) {
    try {
        String json = fetchDataFromApi(iso, date);

        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        JsonArray reports = root.getAsJsonArray("data");

        for (JsonElement element : reports) {
            JsonObject obj = element.getAsJsonObject();
            JsonObject region = obj.getAsJsonObject("region");

            String province = region.get("province").isJsonNull() ? "SIN_PROVINCIA" : region.get("province").getAsString();
            int confirmed = obj.get("confirmed").getAsInt();
            int deaths = obj.get("deaths").getAsInt();
            int recovered = obj.get("recovered").getAsInt();
            int active = obj.get("active").getAsInt();

            CovidReport report = new CovidReport();
            report.setDate(date.toString());
            report.setProvince(province);
            report.setConfirmed(confirmed);
            report.setDeaths(deaths);
            report.setRecovered(recovered);
            report.setActive(active);
            report.setRawDataJson(obj.toString());

            covidReportRepository.save(report);
        }

        logger.info("Datos insertados desde API para " + iso + " en la fecha " + date);

    } catch (Exception e) {
        logger.error("Error al consumir API o guardar datos: " + e.getMessage());
    }
}
    
}
