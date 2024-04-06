package ru.sejapoe.javapr.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sejapoe.javapr.domain.UserEntity;
import ru.sejapoe.javapr.exception.NotFoundException;
import ru.sejapoe.javapr.repo.UserRepository;
import ru.sejapoe.javapr.utils.PredicateUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final EntityManager em;
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

    @Transactional
    public List<UserEntity> filter(String name, LocalDate birthDateBefore, LocalDate birthDateAfter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserEntity> cq = cb.createQuery(UserEntity.class);

        Root<UserEntity> user = cq.from(UserEntity.class);
        List<Predicate> predicates = new ArrayList<>();
        if (name != null) {
            predicates.add(cb.or(
                    PredicateUtils.likeIgnoreCase(cb, user.get("firstName"), name),
                    PredicateUtils.likeIgnoreCase(cb, user.get("lastName"), name),
                    PredicateUtils.likeIgnoreCase(cb, user.get("middleName"), name)
            ));
        }

        if (birthDateBefore != null) {
            predicates.add(
                    cb.lessThan(user.get("birthDate"), birthDateBefore)
            );
        }

        if (birthDateAfter != null) {
            predicates.add(
                    cb.greaterThan(user.get("birthDate"), birthDateAfter)
            );
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return em.createQuery(cq).getResultList();
    }
}
