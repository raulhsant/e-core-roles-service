package com.ecore.rolesservice.mapper;

import com.ecore.rolesservice.TestDataGen;
import com.ecore.rolesservice.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoleMapperTest {

    private RoleMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RoleMapperImpl();
    }

    @Test
    void toRoleResponseBody_whenIsNull_thenReturnNull() {
        final var result = mapper.toRoleResponseBody(null);
        assertThat(result).isNull();
    }

    @Test
    void toRoleResponseBody() {
        final var role = TestDataGen.getObject(Role.class);

        final var result = mapper.toRoleResponseBody(role);

        assertThat(result.getName()).isEqualTo(role.getName());
        assertThat(result.getCreatedAt()).isEqualTo(role.getCreatedAt());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testToRoleResponseBodyList_whenIsNullOrEmpty_theReturnEmptyList(List<Role> roles) {
        final var result = mapper.toRoleResponseBodyList(roles);
        assertThat(result).isEmpty();
    }

    @Test
    void testToRoleResponseBodyList() {
        final var roles = TestDataGen.getListOfObject(Role.class);

        final var result = mapper.toRoleResponseBodyList(roles);

        assertThat(result).hasSameSizeAs(roles);
        assertThat(result.get(0).getName()).isEqualTo(roles.get(0).getName());
        assertThat(result.get(0).getCreatedAt()).isEqualTo(roles.get(0).getCreatedAt());
    }
}
