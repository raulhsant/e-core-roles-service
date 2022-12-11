package com.ecore.rolesservice.mapper;

import com.ecore.rolesservice.AppConstants;
import com.ecore.rolesservice.TestDataGen;
import com.ecore.rolesservice.model.Membership;
import com.ecore.rolesservice.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MembershipMapperTest {

    private MembershipMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new MembershipMapperImpl();
    }

    @Test
    void toMembershipResponseBody_whenMembershipIsNull_thenReturnNull() {
        final var result = mapper.toMembershipResponseBody(null);
        assertThat(result).isNull();
    }

    @Test
    void toMembershipResponseBody_whenRoleIsNull_thenPopulateWithDefaultRole() {
        var membership = TestDataGen.getObject(Membership.class);
        membership.setRole(null);

        final var result = mapper.toMembershipResponseBody(membership);

        assertThat(result.getTeamId()).isEqualTo(membership.getTeamId());
        assertThat(result.getUserId()).isEqualTo(membership.getUserId());
        assertThat(result.getRole()).isEqualTo(AppConstants.DEFAULT_ROLE_NAME);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void toMembershipResponseBody_whenRoleIsEmpty_thenPopulateWithDefaultRole(String roleName) {
        var membership = TestDataGen.getObject(Membership.class);
        membership.setRole(Role.builder().name(roleName).build());

        final var result = mapper.toMembershipResponseBody(membership);

        assertThat(result.getTeamId()).isEqualTo(membership.getTeamId());
        assertThat(result.getUserId()).isEqualTo(membership.getUserId());
        assertThat(result.getRole()).isEqualTo(AppConstants.DEFAULT_ROLE_NAME);
    }

    @Test
    void toMembershipResponseBody() {
        final var membership = TestDataGen.getObject(Membership.class);

        final var result = mapper.toMembershipResponseBody(membership);

        assertThat(result.getTeamId()).isEqualTo(membership.getTeamId());
        assertThat(result.getUserId()).isEqualTo(membership.getUserId());
        assertThat(result.getRole()).isEqualTo(membership.getRole().getName());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testToMembershipResponseBody_whenIsNullOrEmpty_theReturnEmptyList(List<Membership> memberships) {
        final var result = mapper.toMembershipResponseBodyList(memberships);
        assertThat(result).isEmpty();
    }

    @Test
    void testToMembershipResponseBody() {
        final var memberships = TestDataGen.getListOfObject(Membership.class);

        final var result = mapper.toMembershipResponseBodyList(memberships);

        assertThat(result).hasSameSizeAs(memberships);
        assertThat(result.get(0).getTeamId()).isEqualTo(memberships.get(0).getTeamId());
        assertThat(result.get(0).getUserId()).isEqualTo(memberships.get(0).getUserId());
        assertThat(result.get(0).getRole()).isEqualTo(memberships.get(0).getRole().getName());
    }
}