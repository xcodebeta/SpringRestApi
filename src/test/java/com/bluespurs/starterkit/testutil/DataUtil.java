package com.bluespurs.starterkit.testutil;

import com.bluespurs.starterkit.domain.User;
import com.bluespurs.starterkit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import static com.bluespurs.starterkit.testutil.RandomUtil.*;
import static org.springframework.test.jdbc.JdbcTestUtils.*;

@Component
public class DataUtil {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserService userService;

    public int countRows(String table) {
        return countRowsInTable(jdbcTemplate, table);
    }

    public void clearTable(String table) {
        deleteFromTables(jdbcTemplate, table);
    }

    public User createUser() {
        return userService.create(getRandomEmail(), getRandomPassword());
    }

    public User createUser(String email, String password) {
        return userService.create(email, password);
    }
}
