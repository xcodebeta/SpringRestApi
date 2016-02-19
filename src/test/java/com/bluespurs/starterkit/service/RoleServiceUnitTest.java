package com.bluespurs.starterkit.service;

import com.bluespurs.starterkit.UnitTest;
import com.bluespurs.starterkit.domain.Role;
import com.bluespurs.starterkit.repository.RoleRepository;
import com.bluespurs.starterkit.repository.specification.FindRoleByNameSpec;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;

import java.util.Optional;

import static com.bluespurs.starterkit.testutil.RandomUtil.getRandomInt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@Category(UnitTest.class)
public class RoleServiceUnitTest extends UnitTest {
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Before
    public void setUp() {
        super.setUp();
        roleService = new RoleServiceImpl(roleRepository);
    }

    /**
     * Test finding a role by name.
     *
     * @see RoleService#findByName(String)
     */
    @Test
    public void testFindByName() {
        Role role = new Role();
        role.setId(getRandomInt(0, 1000));
        role.setName(Role.USER);

        when(roleRepository.findOne(any(FindRoleByNameSpec.class))).thenReturn(role);
        Optional<Role> actual = roleService.findByName(Role.USER);

        assertThat(actual.isPresent(), equalTo(true));
        assertThat(actual.get(), equalTo(role));
    }

    /**
     * Test finding a role by name when it does not exist.
     *
     * @see RoleService#findByName(String)
     */
    @Test
    public void testFindByName_WhenRoleNotExists() {
        when(roleRepository.findOne(any(FindRoleByNameSpec.class))).thenReturn(null);
        Optional<Role> actual = roleService.findByName(Role.USER);

        assertThat(actual.isPresent(), equalTo(false));
    }
}
