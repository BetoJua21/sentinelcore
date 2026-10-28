package com.robertojuarez.sentinelcore.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String name;
    private String rfidUid;
    private String role;
    private boolean active;

    public User(){};

    public User(String name,String rfidUid,String role,boolean active)
    {
        this.name = name;
        this.rfidUid = rfidUid;
        this.role = role;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRfidUid() {
        return rfidUid;
    }

    public String getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }
}
