package com.bluespurs.starterkit.repository.specification;

import com.bluespurs.starterkit.domain.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FindUserByEmailSpec implements Specification<User> {
    private final String email;

    public FindUserByEmailSpec(String email) {
        this.email = email;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get("email"), email);
    }
}
