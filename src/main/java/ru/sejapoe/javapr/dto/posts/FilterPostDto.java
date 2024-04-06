package ru.sejapoe.javapr.dto.posts;

import jakarta.annotation.Nullable;

public record FilterPostDto(
        @Nullable
        String authorName,
        @Nullable
        String text
) {
}
