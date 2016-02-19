package com.bluespurs.starterkit.service;

import com.bluespurs.starterkit.domain.Role;
import com.bluespurs.starterkit.repository.RoleRepository;
import com.bluespurs.starterkit.repository.specification.FindRoleByNameSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Cacheable(value = "role")
    public Optional<Role> findByName(String role) {
        Specification<Role> findByName = new FindRoleByNameSpec(role);
        return Optional.ofNullable(roleRepository.findOne(findByName));
    }
}
