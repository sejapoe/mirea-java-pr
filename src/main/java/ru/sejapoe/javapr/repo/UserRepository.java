
package ru.sejapoe.javapr.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sejapoe.javapr.domain.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}