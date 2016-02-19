package com.bluespurs.starterkit.security;

import com.bluespurs.starterkit.UnitTest;
import com.bluespurs.starterkit.domain.User;
import com.bluespurs.starterkit.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.bluespurs.starterkit.testutil.RandomUtil.getRandomEmail;
import static com.bluespurs.starterkit.testutil.RandomUtil.getRandomInt;
import static com.bluespurs.starterkit.testutil.RandomUtil.getRandomPassword;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@Category(UnitTest.class)
public class UserDetailsServiceUnitTest extends UnitTest {
    private UserDetailsService userDetailsService;

    @Mock
    private UserService userService;

    @Before
    public void setUp() {
        super.setUp();
        userDetailsService = new UserDetailsServiceImpl(userService);
    }

    /**
     * Test loading a user by username.
     *
     * @see UserDetailsService#loadUserByUsername(String)
     */
    @Test
    public void testLoadUserByUsername() throws UsernameNotFoundException {
        User user = new User();
        user.setId(getRandomInt(0, 1000));
        user.setEmail(getRandomEmail());
        user.setPassword(getRandomPassword());

        when(userService.getUserByEmail(eq(user.getEmail()))).thenReturn(Optional.of(user));

        UserDetails actual = userDetailsService.loadUserByUsername(user.getEmail());
        UserDetails expected = new UserDetailsImpl(user);

        assertThat(actual, equalTo(expected));
    }

    /**
     * Test loading a user by username when no such user exists.
     *
     * @see UserDetailsService#loadUserByUsername(String)
     */
    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsername_WhenUserNotFound() throws UsernameNotFoundException {
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.empty());
        userDetailsService.loadUserByUsername(getRandomEmail());
    }
}
