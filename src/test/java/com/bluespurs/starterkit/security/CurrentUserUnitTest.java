package com.bluespurs.starterkit.security;

import com.bluespurs.starterkit.UnitTest;
import com.bluespurs.starterkit.domain.Role;
import com.bluespurs.starterkit.domain.User;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;

import java.util.Collection;

import static com.bluespurs.starterkit.testutil.RandomUtil.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@Category(UnitTest.class)
public class CurrentUserUnitTest extends UnitTest {
    @Mock
    private SecurityContextFacade securityContextFacade;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private CurrentUser currentUser;

    @Before
    public void setUp() {
        super.setUp();
        currentUser = new CurrentUserImpl(securityContextFacade);
    }

    /**
     * Test checking user authentication.
     *
     * @see CurrentUser#isAuthenticated()
     */
    @Test
    public void testIsAuthenticated() {
        boolean expected = getRandomBoolean();

        when(authentication.isAuthenticated()).thenReturn(expected);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(securityContextFacade.getContext()).thenReturn(securityContext);

        boolean actual = currentUser.isAuthenticated();

        assertThat(actual, equalTo(expected));
    }

    /**
     * Test checking for user roles.
     *
     * @see CurrentUser#hasRole(String)
     */
    @Test
    public void testHasRole() {
        Role user = new Role();
        Role admin = new Role();

        user.setName(Role.USER);
        admin.setName(Role.ADMIN);

        Collection<? extends GrantedAuthority> authorities = Lists.newArrayList(
                new RoleBasedAuthority(user),
                new RoleBasedAuthority(admin)
        );

        doReturn(authorities).when(authentication).getAuthorities();
        when(authentication.isAuthenticated()).thenReturn(true);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(securityContextFacade.getContext()).thenReturn(securityContext);

        assertThat(currentUser.hasRole(Role.USER), equalTo(true));
        assertThat(currentUser.hasRole(Role.ADMIN), equalTo(true));
        assertThat(currentUser.hasRole("not-a-real-role"), equalTo(false));
    }

    /**
     * Test getting the current user.
     *
     * @see CurrentUser#get()
     */
    @Test
    public void testGet() {
        User user = new User();
        user.setId(getRandomInt(0, 1000));
        user.setEmail(getRandomEmail());
        user.setPassword(getRandomPassword());
        user.setRoles(Lists.newArrayList());
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(securityContextFacade.getContext()).thenReturn(securityContext);

        User actual = currentUser.get();
        assertThat(actual, equalTo(user));
    }

    /**
     * Test getting the current user when the user is not authenticated.
     *
     * @see CurrentUser#get()
     */
    @Test(expected = SecurityException.class)
    public void testGet_WhenNotAuthenticated() {
        when(authentication.isAuthenticated()).thenReturn(false);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(securityContextFacade.getContext()).thenReturn(securityContext);

        currentUser.get();
    }
}
