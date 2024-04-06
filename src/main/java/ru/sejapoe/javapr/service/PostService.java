package ru.sejapoe.javapr.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sejapoe.javapr.domain.PostEntity;
import ru.sejapoe.javapr.exception.NotFoundException;
import ru.sejapoe.javapr.repo.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    @Transactional
    public PostEntity getById(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Post with ID [%d] is not found".formatted(id))
        );
    }

    @Transactional
    public List<PostEntity> getAll() {
        return postRepository.findAll();
    }

    @Transactional
    public PostEntity create(PostEntity postEntity) {
        postEntity.setAuthor(userService.getById(postEntity.getAuthor().getId()));
        return postRepository.save(postEntity);
    }

    @Transactional
    public void delete(Long id) {
        postRepository.delete(getById(id));
    }
}
