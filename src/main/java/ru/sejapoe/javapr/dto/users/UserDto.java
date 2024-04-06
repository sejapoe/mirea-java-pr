package ru.sejapoe.javapr.dto.users;

import ru.sejapoe.javapr.domain.UserEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link UserEntity}
 */
public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String middleName,
        Date bithDate
) implements Serializable {
}