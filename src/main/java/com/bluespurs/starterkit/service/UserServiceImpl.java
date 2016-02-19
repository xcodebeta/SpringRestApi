package com.bluespurs.starterkit.service;

import com.bluespurs.starterkit.domain.Role;
import com.bluespurs.starterkit.domain.User;
import com.bluespurs.starterkit.repository.UserRepository;
import com.bluespurs.starterkit.repository.specification.FindUserByEmailSpec;
import com.google.common.collect.Lists;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    public static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    @CacheEvict(value = "user", allEntries = true)
    public User create(String email, String password) {
        User user = new User();
        Role userRole = roleService.findByName(Role.USER)
                .orElseThrow(() -> new IllegalStateException("Unable to assign user role to new user. Has Liquibase run successfully?"));

        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Lists.newArrayList(userRole));

        return userRepository.save(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "user", key = "#user.getId()")
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "user", key = "#user.getId()")
    public void update(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        update(user);
    }

    @Override
    @Cacheable(value = "user", key = "#userId")
    public Optional<User> getUser(long userId) {
        return Optional.ofNullable(userRepository.findOne(userId));
    }

    @Override
    @Transactional
    public Optional<User> getUserByEmail(String email) {
        Specification<User> findByEmail = new FindUserByEmailSpec(email);
        User user = userRepository.findOne(findByEmail);

        if(user != null) {
            // Initialize lazy loaded relationships for single data returns.
            Hibernate.initialize(user.getRoles());
        }

        return Optional.ofNullable(user);
    }

    @Override
    public Page<User> getAllUsers(int currentPage, int itemsPerPage) {
        // Subtract 1 from the current page because PageRequest is 0 indexed.
        if(currentPage < 1) {
            log.warn("Cannot use page index less than 1!");
            currentPage = 1;
        }

        if(itemsPerPage < 0) {
            log.warn("Items per page must be at least 1!");
            itemsPerPage = 1;
        }

        Pageable pageable = new PageRequest(currentPage - 1, itemsPerPage, Sort.Direction.DESC, "id");
        return userRepository.findAll(pageable);
    }
}
