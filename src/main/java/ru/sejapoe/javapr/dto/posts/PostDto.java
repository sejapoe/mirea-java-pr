package ru.sejapoe.javapr.dto.posts;

import ru.sejapoe.javapr.dto.users.UserDto;

import java.time.Instant;

public record PostDto(
        Long id,
        String text,
        Instant creationDate,
        UserDto author
) {
}
