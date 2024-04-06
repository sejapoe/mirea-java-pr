package ru.sejapoe.javapr.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sejapoe.javapr.dto.SuccessResponse;
import ru.sejapoe.javapr.dto.users.CreateUserDto;
import ru.sejapoe.javapr.dto.users.UserDto;
import ru.sejapoe.javapr.mapper.UserMapper;
import ru.sejapoe.javapr.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        return userMapper.toDto(userService.getById(id));
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody CreateUserDto createUserDto) {
        return userMapper.toDto(userService.create(userMapper.toEntity(createUserDto)));
    }

    @DeleteMapping("/{id}")
    public SuccessResponse delete(@PathVariable Long id) {
        userService.delete(id);
        return new SuccessResponse(true, "User with ID [%d] has been deleted".formatted(id));
    }
}
