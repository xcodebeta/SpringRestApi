package com.bluespurs.starterkit.repository.specification;

import com.bluespurs.starterkit.domain.Role;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FindRoleByNameSpec implements Specification<Role> {
    private final String role;

    public FindRoleByNameSpec(String role) {
        this.role = role;
    }

    @Override
    public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get("name"), role);
    }
}
