package com.ecore.rolesservice.controller.v1;

import com.ecore.rolesservice.TestDataGen;
import com.ecore.rolesservice.mapper.MembershipMapper;
import com.ecore.rolesservice.model.Membership;
import com.ecore.rolesservice.model.dto.membership.MembershipResponseBody;
import com.ecore.rolesservice.service.MembershipService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembershipsControllerTest {

    @InjectMocks
    private MembershipsController controller;

    @Mock
    private MembershipService membershipService;
    @Mock
    private MembershipMapper membershipMapper;

    @Test
    void assignRole() {
        final var teamId = TestDataGen.getString();
        final var userId = TestDataGen.getString();
        final var roleName = TestDataGen.getString();
        final var membership = TestDataGen.getObject(Membership.class);
        final var membershipResponse = TestDataGen.getObject(MembershipResponseBody.class);

        when(membershipService.assignRole(anyString(), anyString(), anyString())).thenReturn(membership);
        when(membershipMapper.toMembershipResponseBody(any(Membership.class))).thenReturn(membershipResponse);

        final var response = controller.assignRole(teamId, userId, roleName);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(membershipResponse);

        verify(membershipService).assignRole(roleName, teamId, userId);
        verify(membershipMapper).toMembershipResponseBody(membership);
    }

    @Test
    void verifyRoleForMembership_whenIsNotMember_theReturnNoContent() {
        final var teamId = TestDataGen.getString();
        final var userId = TestDataGen.getString();
        final var roleName = TestDataGen.getString();

        when(membershipService.checkRoleAssigment(anyString(), anyString(), anyString())).thenReturn(Optional.empty());

        final var response = controller.verifyRoleForMembership(teamId, userId, roleName);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();

        verify(membershipService).checkRoleAssigment(roleName, teamId, userId);
        verifyNoInteractions(membershipMapper);
    }

    @Test
    void verifyRoleForMembership_whenIsMember_theReturnMembership() {
        final var teamId = TestDataGen.getString();
        final var userId = TestDataGen.getString();
        final var roleName = TestDataGen.getString();
        final var membership = TestDataGen.getObject(Membership.class);
        final var membershipResponse = TestDataGen.getObject(MembershipResponseBody.class);

        when(membershipService.checkRoleAssigment(anyString(), anyString(), anyString())).thenReturn(Optional.of(membership));
        when(membershipMapper.toMembershipResponseBody(any(Membership.class))).thenReturn(membershipResponse);

        final var response = controller.verifyRoleForMembership(teamId, userId, roleName);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(membershipResponse);

        verify(membershipService).checkRoleAssigment(roleName, teamId, userId);
        verify(membershipMapper).toMembershipResponseBody(membership);
    }

    @Test
    void findMembership() {
        final var teamId = TestDataGen.getString();
        final var userId = TestDataGen.getString();
        final var membership = TestDataGen.getObject(Membership.class);
        final var membershipResponse = TestDataGen.getObject(MembershipResponseBody.class);

        when(membershipService.getMembership(anyString(), anyString())).thenReturn(membership);
        when(membershipMapper.toMembershipResponseBody(any(Membership.class))).thenReturn(membershipResponse);

        final var response = controller.findMembership(teamId, userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(membershipResponse);

        verify(membershipService).getMembership(teamId, userId);
        verify(membershipMapper).toMembershipResponseBody(membership);
    }
}