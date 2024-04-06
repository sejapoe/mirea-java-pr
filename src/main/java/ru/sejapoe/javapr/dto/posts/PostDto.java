package ru.sejapoe.javapr.dto.posts;

import java.time.Instant;

public record PostDto(
        Long id,
        String text,
        Instant creationDate
) {
}
