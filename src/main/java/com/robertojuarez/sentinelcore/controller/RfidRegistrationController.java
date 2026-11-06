
package com.robertojuarez.sentinelcore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/rfid")
public class RfidRegistrationController {

    private String sessionId;
    private String capturedUid;
    private Instant expiresAt;

    @PostMapping("/registration/start")
    public synchronized Map<String, String> startRegistration() {
        sessionId = UUID.randomUUID().toString();
        capturedUid = null;
        expiresAt = Instant.now().plusSeconds(60);

        return Map.of("sessionId", sessionId);
    }

    @GetMapping("/registration/status")
    public synchronized ResponseEntity<?> getStatus(
            @RequestParam String sessionId) {

        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(404)
                    .body(Map.of("status", "EXPIRED"));
        }

        if (capturedUid == null) {
            return ResponseEntity.ok(
                    Map.of("status", "WAITING"));
        }

        String uid = capturedUid;
        clearSession();

        return ResponseEntity.ok(
                Map.of("status", "CAPTURED", "uid", uid));
    }

    @PostMapping("/scan")
    public synchronized ResponseEntity<?> receiveScan(
            @RequestParam String uid) {

        if (uid == null || uid.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "UID obligatorio"));
        }

        if (sessionId == null || expiresAt == null
                || Instant.now().isAfter(expiresAt)) {
            return ResponseEntity.status(409)
                    .body(Map.of("status", "NO_REGISTRATION"));
        }

        capturedUid = uid.trim().toUpperCase();

        return ResponseEntity.ok(
                Map.of("status", "CAPTURED"));
    }

    private boolean isValidSession(String id) {
        return sessionId != null
                && sessionId.equals(id)
                && expiresAt != null
                && Instant.now().isBefore(expiresAt);
    }

    private void clearSession() {
        sessionId = null;
        capturedUid = null;
        expiresAt = null;
    }
}
