package com.bluespurs.starterkit.service;

import com.bluespurs.starterkit.domain.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(String role);
}
