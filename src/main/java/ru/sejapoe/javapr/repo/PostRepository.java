package ru.sejapoe.javapr.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sejapoe.javapr.domain.PostEntity;

public interface PostRepository extends BaseRepository<PostEntity, Long> {
}