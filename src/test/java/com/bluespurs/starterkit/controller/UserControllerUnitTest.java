package com.bluespurs.starterkit.controller;

import com.bluespurs.starterkit.UnitTest;
import com.bluespurs.starterkit.controller.request.UserInfoRequest;
import com.bluespurs.starterkit.controller.resource.assembler.UserResourceAssembler;
import com.bluespurs.starterkit.domain.User;
import com.bluespurs.starterkit.security.CurrentUser;
import com.bluespurs.starterkit.service.UserService;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bluespurs.starterkit.testutil.RandomUtil.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Category(UnitTest.class)
public class UserControllerUnitTest extends UnitTest {
    private MockMvc mockMvc;
    private Gson gson = new Gson();

    @Mock
    private UserService userService;

    @Mock
    private CurrentUser currentUser;

    @Mock
    private Page<User> userDirectory;

    @Before
    public void setUp() {
        super.setUp();

        // Pass an actual, rather than mocked, assembler so that we can verify JSON output.
        UserResourceAssembler userAssembler = new UserResourceAssembler();

        UserController controller = new UserController(userService, currentUser, userAssembler);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /**
     * Test getting a directory of users under ideal conditions.
     *
     * @see UserController#getUserDirectory(int, int)
     */
    @Test
    public void testGetUserDirectory() throws Exception {
        User user1 = new User();
        user1.setEmail(getRandomEmail());
        user1.setPassword(getRandomPassword());
        user1.setId(getRandomInt());

        User user2 = new User();
        user2.setEmail(getRandomEmail());
        user2.setPassword(getRandomPassword());
        user1.setId(getRandomInt());

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(userDirectory.getTotalElements()).thenReturn((long) users.size());
        when(userDirectory.getTotalPages()).thenReturn(1);
        when(userDirectory.getNumber()).thenReturn(0);
        when(userDirectory.getContent()).thenReturn(users);
        when(userService.getAllUsers(anyInt(), anyInt())).thenReturn(userDirectory);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalPages", is(1)))
                .andExpect(jsonPath("currentPage", is(1)))
                .andExpect(jsonPath("totalItems", is(2)))
                .andExpect(jsonPath("content[0].userId", is((int) user1.getId())))
                .andExpect(jsonPath("content[0].email", is(user1.getEmail())))
                .andExpect(jsonPath("content[1].userId", is((int) user2.getId())))
                .andExpect(jsonPath("content[1].email", is(user2.getEmail())));

        verify(userService, times(1)).getAllUsers(anyInt(), anyInt());
    }

    /**
     * Tests getting the user directory when there are multiple pages.
     *
     * @see UserController#getUserDirectory(int, int)
     */
    @Test
    public void testGetUserDirectory_WithPagination() throws Exception {
        List<User> users = new ArrayList<>();

        // Generate 4 users.
        for (int i = 1; i <= 4; i++) {
            User user = new User();

            user.setEmail(getRandomEmail());
            user.setPassword(getRandomPassword());
            user.setId(i);
            users.add(user);
        }

        when(userDirectory.getTotalElements()).thenReturn((long) users.size());
        when(userDirectory.getTotalPages()).thenReturn(2);
        when(userService.getAllUsers(anyInt(), anyInt())).thenReturn(userDirectory);

        // Return the first 2 users on the first page.
        when(userDirectory.getNumber()).thenReturn(0);
        when(userDirectory.getContent()).thenReturn(users.subList(0, 2));

        mockMvc.perform(get("/user?page=1&perPage=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalPages", is(2)))
                .andExpect(jsonPath("totalItems", is(4)))
                .andExpect(jsonPath("currentPage", is(1)))
                .andExpect(jsonPath("content", hasSize(2)));

        verify(userService, times(1)).getAllUsers(anyInt(), anyInt());

        // Return the second 2 users on the second page.
        when(userDirectory.getNumber()).thenReturn(1);
        when(userDirectory.getContent()).thenReturn(users.subList(2, 4));

        mockMvc.perform(get("/user?page=2&perPage=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalPages", is(2)))
                .andExpect(jsonPath("totalItems", is(4)))
                .andExpect(jsonPath("currentPage", is(2)))
                .andExpect(jsonPath("content", hasSize(2)));

        verify(userService, times(2)).getAllUsers(anyInt(), anyInt());
    }

    /**
     * Tests getting the user directory when the perPage argument is out of range.
     *
     * @see UserController#getUserDirectory(int, int)
     */
    @Test
    public void testGetUserDirectory_WithInvalidPerPage() throws Exception {
        int maxPerPage = UserController.MAX_USERS_PER_PAGE;
        int numUsers = maxPerPage + 1;
        List<User> users = new ArrayList<>();

        // Generate numUsers users
        for (int i = 1; i <= numUsers; i++) {
            User user = new User();

            user.setEmail(getRandomEmail());
            user.setPassword(getRandomPassword());
            user.setId(i);
            users.add(user);
        }

        when(userDirectory.getTotalElements()).thenReturn((long) users.size());
        when(userDirectory.getTotalPages()).thenReturn(2);
        when(userDirectory.getNumber()).thenReturn(0);
        when(userDirectory.getContent()).thenReturn(users.subList(0, maxPerPage));
        when(userService.getAllUsers(anyInt(), anyInt())).thenReturn(userDirectory);

        mockMvc.perform(get("/user?perPage=" + numUsers))
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalPages", is(2)))
                .andExpect(jsonPath("totalItems", is(numUsers)))
                .andExpect(jsonPath("currentPage", is(1)))
                .andExpect(jsonPath("content", hasSize(maxPerPage)));

        verify(userService, times(1)).getAllUsers(anyInt(), eq(maxPerPage));
    }

    /**
     * Tests getting a single user's profile under ideal conditions.
     *
     * @see UserController#getUserProfile(long)
     */
    @Test
    public void testGetUserProfile() throws Exception {
        User user = new User();
        user.setEmail(getRandomEmail());
        user.setPassword(getRandomPassword());
        user.setId(getRandomInt());

        when(userService.getUser(anyLong())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/user/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId", is((int) user.getId())))
                .andExpect(jsonPath("email", is(user.getEmail())));

        verify(userService, times(1)).getUser(anyLong());
    }

    /**
     * Tests getting a single user's profile when the ID is out of range.
     *
     * @see UserController#getUserProfile(long)
     */
    @Test
    public void testGetUserProfile_WithInvalidId() throws Exception {
        int userId = getRandomInt();
        when(userService.getUser(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/" + userId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUser(anyLong());
    }

    /**
     * Test registering a new user under ideal conditions.
     *
     * @see UserController#registerNewUser(UserInfoRequest)
     */
    @Test
    public void testRegisterNewUser() throws Exception {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail(getRandomEmail());
        request.setPassword(getRandomPassword());

        User expected = new User();
        expected.setEmail(request.getEmail());
        expected.setPassword(request.getPassword());
        expected.setId(getRandomInt());

        when(userService.create(anyString(), anyString())).thenReturn(expected);

        mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId", is((int) expected.getId())))
                .andExpect(jsonPath("email", is(request.getEmail())));

        verify(userService, times(1)).create(anyString(), anyString());
    }

    /**
     * Test registering a new user with an invalid email address and password.
     *
     * @see UserController#registerNewUser(UserInfoRequest)
     */
    @Test
    public void testRegisterNewUser_WithInvalidParameters() throws Exception {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("notanemail");
        request.setPassword("123456");

        mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(request)))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(userService);
    }

    /**
     * Test registering a new user with a duplicate email address.
     *
     * @see UserController#registerNewUser(UserInfoRequest)
     */
    @Test
    public void testRegisterNewUser_WithDuplicateEmail() throws Exception {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail(getRandomEmail());
        request.setPassword(getRandomPassword());

        when(userService.create(anyString(), anyString())).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(request)))
                .andExpect(status().isConflict());

        verify(userService, times(1)).create(anyString(), anyString());
    }

    /**
     * Test updating a user.
     *
     * @see UserController#updateUserInfo(UserInfoRequest)
     */
    @Test
    public void testUpdateUserInfo() throws Exception {
        User originalUser = new User();
        originalUser.setId(getRandomInt(0, 1000));
        originalUser.setEmail(getRandomEmail());
        originalUser.setPassword(getRandomPassword());

        when(currentUser.get()).thenReturn(originalUser);

        UserInfoRequest request = new UserInfoRequest();
        request.setEmail(getRandomEmail());
        request.setPassword(getRandomPassword());

        mockMvc.perform(put("/user").contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId", is((int) originalUser.getId())))
                .andExpect(jsonPath("email", is(request.getEmail())));

        verify(userService, times(1)).update(anyObject(), eq(request.getPassword()));
    }

    /**
     * Test updating a user that attempts to use another user's email address.
     *
     * @see UserController#updateUserInfo(UserInfoRequest)
     */
    @Test
    public void testUpdateUserInfo_WithDuplicateEmail() throws Exception {
        User originalUser = new User();
        originalUser.setId(getRandomInt(0, 1000));
        originalUser.setEmail(getRandomEmail());
        originalUser.setPassword(getRandomPassword());

        when(currentUser.get()).thenReturn(originalUser);

        UserInfoRequest request = new UserInfoRequest();
        request.setEmail(getRandomEmail());
        request.setPassword(getRandomPassword());

        doThrow(DataIntegrityViolationException.class).when(userService).update(anyObject(), anyString());

        mockMvc.perform(put("/user").contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(request)))
                .andExpect(status().isConflict());

        verify(userService, times(1)).update(anyObject(), eq(request.getPassword()));
    }

    /**
     * Test accessing the current user details.
     *
     * @see UserController#getCurrentUser()
     */
    @Test
    public void testGetCurrentUser() throws Exception {
        User expectedUser = new User();
        expectedUser.setId(getRandomInt(0, 1000));
        expectedUser.setEmail(getRandomEmail());
        expectedUser.setPassword(getRandomPassword());

        when(currentUser.get()).thenReturn(expectedUser);

        mockMvc.perform(get("/user/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId", is((int) expectedUser.getId())))
                .andExpect(jsonPath("email", is(expectedUser.getEmail())));

        verify(currentUser, times(1)).get();
    }
}
