package ru.sejapoe.javapr.dto.users;

import jakarta.annotation.Nullable;

import java.time.LocalDate;

public record FilterUserDto(
        @Nullable
        String name,
        @Nullable
        LocalDate birthDateBefore,
        @Nullable
        LocalDate birthDateAfter
) {
}