package com.robertojuarez.sentinelcore.dto;

public record CreateUserRequest(
        String name,
        String rfidUid,
        String role
) {
}
