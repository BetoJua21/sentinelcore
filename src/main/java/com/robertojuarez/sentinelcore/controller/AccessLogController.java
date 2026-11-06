
package com.robertojuarez.sentinelcore.controller;

import com.robertojuarez.sentinelcore.model.AccessLog;
import com.robertojuarez.sentinelcore.repository.AccessLogRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/logs")
public class AccessLogController {

    private final AccessLogRepository accessLogRepository;

    public AccessLogController(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }

    @GetMapping
    public Page<AccessLog> getAllLogs(
            @RequestParam(required = false) String rfidUid,
            @RequestParam(required = false) Boolean granted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC,"timestamp")
        );

        if (rfidUid != null && granted != null) {
            return accessLogRepository.findByRfidUidAndGranted(
                    rfidUid, granted, pageable);
        }

        if (rfidUid != null) {
            return accessLogRepository.findByRfidUid(
                    rfidUid, pageable);
        }

        if (Boolean.TRUE.equals(granted)) {
            return accessLogRepository.findByGrantedTrue(pageable);
        }

        if (Boolean.FALSE.equals(granted)) {
            return accessLogRepository.findByGrantedFalse(pageable);
        }

        return accessLogRepository.findAll(pageable);
    }
}
