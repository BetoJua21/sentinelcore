package com.robertojuarez.sentinelcore.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "access_logs")
public class AccessLog {

    @Id
    private String id;
    private String rfidUid;
    private LocalDateTime timestamp;
    private boolean granted;
    private String message;

    public AccessLog(){}

    public AccessLog(String rfidUid, LocalDateTime timestamp, boolean granted, String message) {
        this.rfidUid = rfidUid;
        this.timestamp = timestamp;
        this.granted = granted;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRfidUid() {
        return rfidUid;
    }

    public void setRfidUid(String rfidUid) {
        this.rfidUid = rfidUid;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isGranted() {
        return granted;
    }

    public void setGranted(boolean granted) {
        this.granted = granted;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
