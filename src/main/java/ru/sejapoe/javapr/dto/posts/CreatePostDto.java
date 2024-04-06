package ru.sejapoe.javapr.dto.posts;

import jakarta.validation.constraints.NotBlank;

public record CreatePostDto(
        @NotBlank String text
) {

}
