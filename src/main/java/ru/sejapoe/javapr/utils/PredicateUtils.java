package ru.sejapoe.javapr.utils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import java.util.Locale;

public class PredicateUtils {
    public static Predicate likeIgnoreCase(CriteriaBuilder cb, Path<String> path, String authorName) {
        return cb.like(cb.lower(path), "%" + authorName.toLowerCase(Locale.ROOT) + "%");
    }
}
