
package com.robertojuarez.sentinelcore.controller;

import com.robertojuarez.sentinelcore.model.AccessLog;
import com.robertojuarez.sentinelcore.service.AccessLogService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/accesses")
public class AccessesController {

    private final AccessLogService accessLogService;

    public AccessesController(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    @GetMapping
    public String accesses(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) String rfidUid,
            @RequestParam(required = false) Boolean granted,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        int safePage = Math.max(page, 0);

        Page<AccessLog> logs = accessLogService.searchLogs(
                date,
                rfidUid,
                granted,
                safePage,
                10
        );

        model.addAttribute("logs", logs);
        model.addAttribute("date", date);
        model.addAttribute("rfidUid", rfidUid);
        model.addAttribute("granted", granted);

        return "accesses";
    }
}
