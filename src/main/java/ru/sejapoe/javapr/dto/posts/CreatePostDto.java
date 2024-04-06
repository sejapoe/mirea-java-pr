package ru.sejapoe.javapr.dto.posts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePostDto(
        @NotBlank String text,
        @NotNull Long authorId
) {

}
