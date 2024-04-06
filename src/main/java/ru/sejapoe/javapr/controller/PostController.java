package ru.sejapoe.javapr.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
    public List<PostDto> getAll() {
        return postService.getAll().stream().map(postMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PostDto get(@PathVariable Long id) {
        return postMapper.toDto(postService.getById(id));
    }

    @PostMapping
    public PostDto create(@Valid @RequestBody CreatePostDto createPostDto) {
        return postMapper.toDto(postService.create(postMapper.toEntity(createPostDto)));
    }

    @DeleteMapping("/{id}")
    public SuccessResponse delete(@PathVariable Long id) {
        postService.delete(id);
        return new SuccessResponse(true, "Post with ID [%d] has been deleted".formatted(id));
    }

    @GetMapping("/filter")
    public List<PostDto> filter(@ParameterObject FilterPostDto filterPostDto) {
        return postService.filter(filterPostDto.authorName(), filterPostDto.text()).stream().map(postMapper::toDto).collect(Collectors.toList());
    }
}
