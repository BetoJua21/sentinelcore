
package com.robertojuarez.sentinelcore.controller;

import com.robertojuarez.sentinelcore.model.AccessLog;
import com.robertojuarez.sentinelcore.repository.AccessLogRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Controller
public class ReportsController {

    private final AccessLogRepository repository;

    private static final ZoneId ZONE =
            ZoneId.of("America/Mexico_City");

    public ReportsController(AccessLogRepository repository) {
        this.repository = repository;
    }

    private List<AccessLog> getFilteredLogs(
            LocalDate startDate,
            LocalDate endDate) {

        Instant start = startDate.atStartOfDay(ZONE).toInstant();
        Instant end = endDate.plusDays(1)
                .atStartOfDay(ZONE).toInstant();

        return repository.findAccessLogsBetweenDates(start, end)
                .stream()
                .sorted(Comparator.comparing(
                        AccessLog::getTimestamp).reversed())
                .toList();
    }

    @GetMapping("/reports")
    public String reports(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            Model model) {

        LocalDate today = LocalDate.now(ZONE);

        if (startDate == null) {
            startDate = today.minusDays(6);
        }

        if (endDate == null) {
            endDate = today;
        }

        if (startDate.isAfter(endDate)) {
            model.addAttribute("error",
                    "La fecha inicial no puede ser posterior a la final.");
            model.addAttribute("logs", List.of());
            model.addAttribute("total", 0);
            model.addAttribute("granted", 0);
            model.addAttribute("denied", 0);
        } else {
            List<AccessLog> logs =
                    getFilteredLogs(startDate, endDate);

            long granted = logs.stream()
                    .filter(AccessLog::isGranted)
                    .count();

            model.addAttribute("logs", logs);
            model.addAttribute("total", logs.size());
            model.addAttribute("granted", granted);
            model.addAttribute("denied",
                    logs.size() - granted);
        }

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "reports";
    }

    @GetMapping("/reports/export")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().build();
        }

        List<AccessLog> logs =
                getFilteredLogs(startDate, endDate);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .withZone(ZONE);

        StringBuilder csv = new StringBuilder();
        csv.append("Fecha,RFID,Resultado,Mensaje\r\n");

        for (AccessLog log : logs) {
            csv.append(csvEscape(
                            formatter.format(log.getTimestamp())))
                    .append(",");

            csv.append(csvEscape(log.getRfidUid()))
                    .append(",");

            csv.append(log.isGranted()
                            ? "Concedido" : "Denegado")
                    .append(",");

            csv.append(csvEscape(log.getMessage()))
                    .append("\r\n");
        }

        byte[] content = ("\uFEFF" + csv)
                .getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"sentinelcore-report.csv\""
                )
                .contentType(new MediaType(
                        "text", "csv", StandardCharsets.UTF_8))
                .body(content);
    }

    private String csvEscape(String value) {
        if (value == null) {
            return "\"\"";
        }

        String safe = value.replace("\"", "\"\"");

        if (!safe.isEmpty() &&
                "=+-@\t\r\n".indexOf(safe.charAt(0)) >= 0) {
            safe = "'" + safe;
        }

        return "\"" + safe + "\"";
    }
}
