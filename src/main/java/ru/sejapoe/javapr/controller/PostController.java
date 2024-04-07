package ru.sejapoe.javapr.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sejapoe.javapr.dto.SuccessResponse;
import ru.sejapoe.javapr.dto.posts.CreatePostDto;
import ru.sejapoe.javapr.dto.posts.FilterPostDto;
import ru.sejapoe.javapr.dto.posts.PostDto;
import ru.sejapoe.javapr.mapper.PostMapper;
import ru.sejapoe.javapr.service.PostService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostMapper postMapper;
    private final PostService postService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public List<PostDto> getAll() {
        return postService.getAll().stream().map(postMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public PostDto get(@PathVariable Long id) {
        return postMapper.toDto(postService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public PostDto create(@Valid @RequestBody CreatePostDto createPostDto) {
        return postMapper.toDto(postService.create(postMapper.toEntity(createPostDto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SuccessResponse delete(@PathVariable Long id) {
        postService.delete(id);
        return new SuccessResponse(true, "Post with ID [%d] has been deleted".formatted(id));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('USER')")
    public List<PostDto> filter(@ParameterObject FilterPostDto filterPostDto) {
        return postService.filter(filterPostDto.authorName(), filterPostDto.text()).stream().map(postMapper::toDto).collect(Collectors.toList());
    }
}
