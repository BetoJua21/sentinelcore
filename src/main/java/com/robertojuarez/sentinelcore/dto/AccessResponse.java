package com.robertojuarez.sentinelcore.dto;

public record AccessResponse(
        boolean granted,
        String message
) {}

