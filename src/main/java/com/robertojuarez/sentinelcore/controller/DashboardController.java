
package com.robertojuarez.sentinelcore.controller;

import com.robertojuarez.sentinelcore.model.AccessLog;
import com.robertojuarez.sentinelcore.repository.AccessLogRepository;
import com.robertojuarez.sentinelcore.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DashboardController {

    private final UserRepository userRepository;
    private final AccessLogRepository accessLogRepository;

    public DashboardController(
            UserRepository userRepository,
            AccessLogRepository accessLogRepository) {
        this.userRepository = userRepository;
        this.accessLogRepository = accessLogRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("activeUsers", userRepository.countByActiveTrue());
        model.addAttribute("grantedAccess", accessLogRepository.countByGrantedTrue());
        model.addAttribute("deniedAccess", accessLogRepository.countByGrantedFalse());

        PageRequest pageable = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "timestamp")
        );

        List<AccessLog> recentLogs = accessLogRepository
                .findAll(pageable)
                .getContent();

        model.addAttribute("recentLogs", recentLogs);

        ZoneId zone = ZoneId.of("America/Mexico_City");
        LocalDate today = LocalDate.now(zone);
        LocalDate startDate = today.minusDays(6);

        Instant start = startDate.atStartOfDay(zone).toInstant();
        Instant end = today.plusDays(1).atStartOfDay(zone).toInstant();

        List<AccessLog> weeklyLogs =
                accessLogRepository.findAccessLogsBetweenDates(start, end);

        List<String> chartLabels = new ArrayList<>();
        List<String> chartDates = new ArrayList<>();
        List<Long> grantedCounts = new ArrayList<>();
        List<Long> deniedCounts = new ArrayList<>();

        for (int i = 0; i < 7; i++) {

            LocalDate date = startDate.plusDays(i);

            long granted = 0;
            long denied = 0;

            for (AccessLog log : weeklyLogs) {

                LocalDate logDate = log.getTimestamp()
                        .atZone(zone)
                        .toLocalDate();

                if (logDate.equals(date)) {
                    if (log.isGranted()) {
                        granted++;
                    } else {
                        denied++;
                    }
                }
            }

            chartLabels.add(
                    date.getDayOfWeek()
                            .getDisplayName(
                                    java.time.format.TextStyle.SHORT,
                                    java.util.Locale.forLanguageTag("es-MX")
                            )
            );

            chartDates.add(date.toString());
            grantedCounts.add(granted);
            deniedCounts.add(denied);
        }

        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartDates", chartDates);
        model.addAttribute("grantedCounts", grantedCounts);
        model.addAttribute("deniedCounts", deniedCounts);

        return "dashboard";
    }
}
