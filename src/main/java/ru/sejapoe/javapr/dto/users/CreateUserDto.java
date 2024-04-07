package ru.sejapoe.javapr.dto.users;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import ru.sejapoe.javapr.domain.UserEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link UserEntity}
 */
public record CreateUserDto(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String middleName,
        @NotNull @Past Date bithDate
) implements Serializable {
}