package ru.sejapoe.javapr.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sejapoe.javapr.domain.UserEntity;
import ru.sejapoe.javapr.exception.NotFoundException;
import ru.sejapoe.javapr.repo.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserEntity getById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("User with ID [%d] is not found".formatted(id))
        );
    }

    @Transactional
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    public UserEntity create(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.delete(getById(id));
    }
}
