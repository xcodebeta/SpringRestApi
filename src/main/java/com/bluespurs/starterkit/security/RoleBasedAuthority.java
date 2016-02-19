package com.bluespurs.starterkit.security;

import com.bluespurs.starterkit.domain.Role;
import org.springframework.security.core.GrantedAuthority;

public class RoleBasedAuthority implements GrantedAuthority {
    private static final long serialVersionUID = -4962102794064958004L;

    private final Role role;

    public RoleBasedAuthority(Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.getName();
    }
}
