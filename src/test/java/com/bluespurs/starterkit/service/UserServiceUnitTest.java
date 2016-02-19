package com.bluespurs.starterkit.service;

import com.bluespurs.starterkit.UnitTest;
import com.bluespurs.starterkit.domain.Role;
import com.bluespurs.starterkit.domain.User;
import com.bluespurs.starterkit.repository.UserRepository;
import com.bluespurs.starterkit.repository.specification.FindUserByEmailSpec;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.bluespurs.starterkit.testutil.RandomUtil.getRandomEmail;
import static com.bluespurs.starterkit.testutil.RandomUtil.getRandomInt;
import static com.bluespurs.starterkit.testutil.RandomUtil.getRandomPassword;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Category(UnitTest.class)
public class UserServiceUnitTest extends UnitTest {
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;

    @Before
    public void setUp() {
        super.setUp();
        userService = new UserServiceImpl(userRepository, roleService, passwordEncoder);
    }

    /**
     * Test creating a new user.
     *
     * @see UserService#create(String, String)
     */
    @Test
    public void testCreate() {
        Role role = new Role();
        role.setName(Role.USER);

        when(roleService.findByName(eq(Role.USER))).thenReturn(Optional.of(role));

        User expected = new User();
        expected.setId(getRandomInt(0, 1000));
        expected.setEmail(getRandomEmail());
        expected.setPassword(getRandomPassword());
        expected.setRoles(Lists.newArrayList(role));

        when(passwordEncoder.encode(anyString())).thenReturn(expected.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(expected);

        User actual = userService.create(expected.getEmail(), expected.getPassword());

        assertThat(actual, equalTo(expected));
    }

    /**
     * Test creating an account with a duplicate email.
     *
     * @see UserService#create(String, String)
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void testCreate_WithDuplicateEmail() {
        Role role = new Role();
        role.setName(Role.USER);

        when(roleService.findByName(eq(Role.USER))).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        userService.create(getRandomEmail(), getRandomPassword());
    }

    /**
     * Test updating a user.
     *
     * @see UserService#update(User)
     */
    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(getRandomInt(0, 1000));
        user.setEmail(getRandomEmail());
        user.setPassword(getRandomPassword());
        user.setRoles(Lists.newArrayList());

        userService.update(user);
        verify(userRepository, times(1)).save(eq(user));
    }

    /**
     * Test updating a user with a duplicate email.
     *
     * @see UserService#update(User)
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdate_WithDuplicateEmail() {
        User user = new User();
        user.setId(getRandomInt(0, 1000));
        user.setEmail(getRandomEmail());
        user.setPassword(getRandomPassword());
        user.setRoles(Lists.newArrayList());

        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);
        userService.update(user);
    }

    /**
     * Tests updating a user when supplying a new password.
     *
     * @see UserService#update(User, String)
     */
    @Test
    public void testUpdate_WithNewPassword() {
        User user = new User();
        user.setId(getRandomInt(0, 1000));
        user.setEmail(getRandomEmail());
        user.setPassword(getRandomPassword());
        user.setRoles(Lists.newArrayList());

        String newPassword = getRandomPassword();

        userService.update(user, newPassword);

        verify(passwordEncoder, times(1)).encode(eq(newPassword));
        verify(userRepository, times(1)).save(eq(user));
    }

    /**
     * Test getting a specific user.
     *
     * @see UserService#getUser(long)
     */
    @Test
    public void testGetUser() {
        User expected = new User();
        expected.setId(getRandomInt(0, 1000));
        expected.setEmail(getRandomEmail());
        expected.setPassword(getRandomPassword());
        expected.setRoles(Lists.newArrayList());

        when(userRepository.findOne(eq(expected.getId()))).thenReturn(expected);
        Optional<User> actual = userService.getUser(expected.getId());

        assertThat(actual.isPresent(), equalTo(true));
        assertThat(actual.get(), equalTo(expected));
    }

    /**
     * Test getting a specific user that does not exist.
     *
     * @see UserService#getUser(long)
     */
    @Test
    public void testGetUser_WhenUserNotExists() {
        when(userRepository.findOne(anyLong())).thenReturn(null);
        Optional<User> actual = userService.getUser(getRandomInt());

        assertThat(actual.isPresent(), equalTo(false));
    }

    /**
     * Test getting a user by email address.
     *
     * @see UserService#getUserByEmail(String)
     */
    @Test
    public void testGetUserByEmail() {
        User expected = new User();
        expected.setId(getRandomInt(0, 1000));
        expected.setEmail(getRandomEmail());
        expected.setPassword(getRandomPassword());
        expected.setRoles(Lists.newArrayList());

        when(userRepository.findOne(any(FindUserByEmailSpec.class))).thenReturn(expected);
        Optional<User> actual = userService.getUserByEmail(expected.getEmail());

        assertThat(actual.isPresent(), equalTo(true));
        assertThat(actual.get(), equalTo(expected));
    }

    /**
     * Test getting a user by email address that doesn't exist.
     *
     * @see UserService#getUserByEmail(String)
     */
    @Test
    public void testGetUserByEmail_WhenUserNotExists() {
        when(userRepository.findOne(any(FindUserByEmailSpec.class))).thenReturn(null);
        Optional<User> actual = userService.getUserByEmail(getRandomEmail());

        assertThat(actual.isPresent(), equalTo(false));
    }

    /**
     * Test getting all users.
     *
     * @see UserService#getAllUsers(int, int)
     */
    @Test
    public void testGetAllUsers() {
        userService.getAllUsers(1, 10);
        verify(userRepository, times(1)).findAll(any(Pageable.class));
    }

    /**
     * Test getting all users when the page number is invalid.
     *
     * @see UserService#getAllUsers(int, int)
     */
    @Test
    public void testGetUsers_WithInvalidPageNum() {
        userService.getAllUsers(0, 10);
        verify(userRepository, times(1)).findAll(any(Pageable.class));

        userService.getAllUsers(-9999, 10);
        verify(userRepository, times(2)).findAll(any(Pageable.class));
    }

    /**
     * Test getting all users when the amount of users per page is invalid.
     *
     * @see UserService#getAllUsers(int, int)
     */
    @Test
    public void testGetUsers_WithInvalidItemsPerPage() {
        userService.getAllUsers(1, -1);
        verify(userRepository, times(1)).findAll(any(Pageable.class));

        userService.getAllUsers(1, -9999);
        verify(userRepository, times(2)).findAll(any(Pageable.class));
    }
}
