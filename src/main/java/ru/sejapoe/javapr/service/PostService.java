package ru.sejapoe.javapr.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sejapoe.javapr.domain.PostEntity;
import ru.sejapoe.javapr.exception.NotFoundException;
import ru.sejapoe.javapr.repo.PostRepository;
import ru.sejapoe.javapr.utils.PredicateUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final EntityManager em;
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

    @Transactional
    public List<PostEntity> filter(String authorName, String text) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PostEntity> cq = cb.createQuery(PostEntity.class);

        Root<PostEntity> post = cq.from(PostEntity.class);
        List<Predicate> predicates = new ArrayList<>();


        if (authorName != null) {
            predicates.add(cb.or(
                    PredicateUtils.likeIgnoreCase(cb, post.get("author").get("firstName"), authorName),
                    PredicateUtils.likeIgnoreCase(cb, post.get("author").get("lastName"), authorName),
                    PredicateUtils.likeIgnoreCase(cb, post.get("author").get("middleName"), authorName)
            ));
        }
        if (text != null) {
            predicates.add(
                    cb.like(post.get("text"), text)
            );
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return em.createQuery(cq).getResultList();
    }
}
