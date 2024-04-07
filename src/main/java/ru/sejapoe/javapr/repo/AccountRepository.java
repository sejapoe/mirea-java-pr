package ru.sejapoe.javapr.repo;

import ru.sejapoe.javapr.domain.AccountEntity;

import java.util.Optional;

public interface AccountRepository extends BaseRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByUsername(String username);
}