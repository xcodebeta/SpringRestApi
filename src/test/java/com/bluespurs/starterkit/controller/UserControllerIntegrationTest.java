package com.bluespurs.starterkit.controller;

import com.bluespurs.starterkit.IntegrationTest;
import com.bluespurs.starterkit.controller.request.UserInfoRequest;
import com.bluespurs.starterkit.domain.User;
import com.bluespurs.starterkit.testutil.DataUtil;
import com.google.common.collect.Lists;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.bluespurs.starterkit.testutil.RandomUtil.*;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Category(IntegrationTest.class)
public class UserControllerIntegrationTest extends IntegrationTest {
    @Autowired
    private DataUtil dataUtil;

    /**
     * Register an account with valid information.
     *
     * @see UserController#registerNewUser(UserInfoRequest)
     */
    @Test
    public void testRegisterNewUser() {
        int expectedRecords = dataUtil.countRows("user") + 1;

        String email = getRandomEmail();
        String password = getRandomPassword();
        UserInfoRequest request = new UserInfoRequest();

        request.setEmail(email);
        request.setPassword(password);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/user")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("userId", isA(Integer.class))
                .body("email", equalTo(email));

        int actualRecords = dataUtil.countRows("user");

        assertThat(actualRecords, equalTo(expectedRecords));
    }

    /**
     * Attempt to register an account with an invalid email address and password.
     *
     * @see UserController#registerNewUser(UserInfoRequest)
     */
    @Test
    public void testRegisterNewUser_WithInvalidInput() {
        int expectedRecords = dataUtil.countRows("user");

        String email = getRandomString(0, 10);
        String password = getRandomString(0, 7);
        UserInfoRequest request = new UserInfoRequest();

        request.setEmail(email);
        request.setPassword(password);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/user")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);

        int actualRecords = dataUtil.countRows("user");

        assertThat(actualRecords, equalTo(expectedRecords));
    }

    /**
     * Attempt to register a new user account with an email address that is already in use.
     *
     * @see UserController#registerNewUser(UserInfoRequest)
     */
    @Test
    public void testRegisterNewUser_WithDuplicateEmail() {
        String email = getRandomEmail();
        String password = getRandomPassword();

        // Create the first user.
        dataUtil.createUser(email, password);

        // Attempt to create a user with the same email address.
        int expectedRecords = dataUtil.countRows("user");
        UserInfoRequest request = new UserInfoRequest();

        request.setEmail(email);
        request.setPassword(password);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/user")
                .then()
                .statusCode(HttpStatus.SC_CONFLICT);

        int actualRecords = dataUtil.countRows("user");

        assertThat(actualRecords, equalTo(expectedRecords));
    }

    /**
     * Get information about a specific user under valid circumstances.
     *
     * @see UserController#getUserProfile(long)
     */
    @Test
    public void testGetUserProfile() {
        User user = dataUtil.createUser();

        when()
                .get("/user/{id}", user.getId())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("userId", equalTo((int) user.getId()))
                .body("email", equalTo(user.getEmail()));
    }

    /**
     * Attempt to get user information when the user does not exist.
     *
     * @see UserController#getUserProfile(long)
     */
    @Test
    public void testGetUserProfile_WhenUserNotExists() {
        when()
                .get("/user/{id}", Integer.MAX_VALUE)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    /**
     * Get information about the currently authenticated user.
     *
     * @see UserController#getCurrentUser()
     */
    @Test
    public void testGetCurrentUser() {
        String email = getRandomEmail();
        String password = getRandomPassword();
        User user = dataUtil.createUser(email, password);

        given()
                .auth().basic(email, password)
                .when()
                .get("/user/current")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("userId", equalTo((int) user.getId()))
                .body("email", equalTo(user.getEmail()));
    }

    /**
     * Attempt to get current user information if the user is not logged in.
     *
     * @see UserController#getCurrentUser()
     */
    @Test
    public void testGetCurrentUser_WhenUserNotAuthenticated() {
        when()
                .get("/user/current")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    /**
     * Attempt to get current user information if the user enters invalid credentials.
     *
     * @see UserController#getCurrentUser()
     */
    @Test
    public void testGetCurrentUser_WhenUserAuthenticationFails() {
        String email = getRandomEmail();
        String password = getRandomPassword();

        given()
                .auth().basic(email, password + "x")
                .when()
                .get("/user/current")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);

        given()
                .auth().basic(email + "x", password)
                .when()
                .get("/user/current")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);

        given()
                .auth().basic(email + "x", password + "x")
                .when()
                .get("/user/current")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    /**
     * Update user information.
     *
     * @see UserController#updateUserInfo(UserInfoRequest)
     */
    @Test
    public void testUpdateUserInfo() {
        String email = getRandomEmail();
        String password = getRandomPassword();
        String newEmail = getRandomEmail();
        String newPassword = getRandomPassword();

        User user = dataUtil.createUser(email, password);
        UserInfoRequest request = new UserInfoRequest();

        request.setEmail(newEmail);
        request.setPassword(newPassword);

        given()
                .auth().basic(email, password)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/user")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("userId", equalTo((int) user.getId()))
                .body("email", equalTo(newEmail));

        // Make sure the password change took effect.
        given()
                .auth().basic(newEmail, newPassword)
                .when()
                .get("/user/current")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    /**
     * Attempt to update user information without authenticating.
     *
     * @see UserController#updateUserInfo(UserInfoRequest)
     */
    @Test
    public void testUpdateUserInfo_WhenUserIsNotAuthenticated() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail(getRandomEmail());
        request.setPassword(getRandomPassword());

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/user")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    /**
     * Attempt to submit invalid information to update a user.
     *
     * @see UserController#updateUserInfo(UserInfoRequest)
     */
    @Test
    public void testUpdateUserInfo_WithInvalidInput() {
        String email = getRandomEmail();
        String password = getRandomPassword();
        dataUtil.createUser(email, password);

        // Leave fields blank.
        UserInfoRequest request = new UserInfoRequest();

        given()
                .auth().basic(email, password)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/user")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * Attempt to update an account using an email address taken by another user.
     *
     * @see UserController#updateUserInfo(UserInfoRequest)
     */
    @Test
    public void testUpdateUserInfo_WithDuplicateEmail() {
        String duplicateEmail = getRandomEmail();
        dataUtil.createUser(duplicateEmail, getRandomPassword());

        String email = getRandomEmail();
        String password = getRandomPassword();
        dataUtil.createUser(email, password);

        // Leave fields blank.
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail(duplicateEmail);
        request.setPassword(password);

        given()
                .auth().basic(email, password)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/user")
                .then()
                .statusCode(HttpStatus.SC_CONFLICT);
    }

    /**
     * Test getting the user directory.
     *
     * @see UserController#getUserDirectory(int, int)
     */
    @Test
    public void testGetUserDirectory() {
        dataUtil.clearTable("user_role");
        dataUtil.clearTable("user");

        int usersPerPage = 10;
        int numUsers = usersPerPage + 1;
        List<User> users = Lists.newArrayList();

        // Generate the users.
        for (int i = 0; i < numUsers; i++) {
            users.add(dataUtil.createUser(getRandomEmail(), getRandomPassword()));
        }

        when()
                .get("/user?page={page}&perPage={perPage}", 1, Integer.MAX_VALUE)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("totalPages", equalTo(1))
                .body("currentPage", equalTo(1))
                .body("totalItems", equalTo(numUsers))
                .body("content", hasSize(numUsers));

        when()
                .get("/user?page={page}&perPage={perPage}", 1, -1)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("totalPages", equalTo(numUsers))
                .body("currentPage", equalTo(1))
                .body("totalItems", equalTo(numUsers))
                .body("content", hasSize(1));
    }

    /**
     * Test getting the user directory with an out of range perPage value.
     *
     * @see UserController#getUserDirectory(int, int)
     */
    @Test
    public void testGetUserDirectory_WithPerPageOutOfRange() {
        dataUtil.clearTable("user_role");
        dataUtil.clearTable("user");

        int usersPerPage = 10;
        int numUsers = usersPerPage + 1;
        List<User> users = Lists.newArrayList();

        // Generate the users.
        for (int i = 0; i < numUsers; i++) {
            users.add(dataUtil.createUser(getRandomEmail(), getRandomPassword()));
        }

        // Get the first page.
        when()
                .get("/user?page={page}&perPage={perPage}", 1, usersPerPage)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("totalPages", equalTo(2))
                .body("currentPage", equalTo(1))
                .body("totalItems", equalTo(numUsers))
                .body("content", hasSize(usersPerPage));

        // Get the second page.
        when()
                .get("/user?page={page}&perPage={perPage}", 2, usersPerPage)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("totalPages", equalTo(2))
                .body("currentPage", equalTo(2))
                .body("totalItems", equalTo(numUsers))
                .body("content", hasSize(1));
    }

    /**
     * Test getting the user directory with an out of range pageNum value.
     *
     * @see UserController#getUserDirectory(int, int)
     */
    @Test
    public void testGetUserDirectory_WithPageNumOutOfRange() {
        dataUtil.clearTable("user_role");
        dataUtil.clearTable("user");

        int usersPerPage = 10;
        int numUsers = usersPerPage;
        List<User> users = Lists.newArrayList();

        // Generate the users.
        for (int i = 0; i < numUsers; i++) {
            users.add(dataUtil.createUser(getRandomEmail(), getRandomPassword()));
        }

        when()
                .get("/user?page={page}&perPage={perPage}", 2, usersPerPage)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("totalPages", equalTo(1))
                .body("currentPage", equalTo(2))
                .body("totalItems", equalTo(numUsers))
                .body("content", hasSize(0));

        when()
                .get("/user?page={page}&perPage={perPage}", 0, usersPerPage)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("totalPages", equalTo(1))
                .body("currentPage", equalTo(1))
                .body("totalItems", equalTo(numUsers))
                .body("content", hasSize(numUsers));
    }
}
