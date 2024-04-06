package ru.sejapoe.javapr.dto;

import jakarta.validation.constraints.NotNull;

public record SuccessResponse(
        @NotNull
        boolean success,
        String message
) {
}
