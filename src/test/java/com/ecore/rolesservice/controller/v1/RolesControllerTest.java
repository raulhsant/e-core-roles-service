package com.ecore.rolesservice.controller.v1;

import com.ecore.rolesservice.TestDataGen;
import com.ecore.rolesservice.mapper.MembershipMapper;
import com.ecore.rolesservice.mapper.RoleMapper;
import com.ecore.rolesservice.model.Membership;
import com.ecore.rolesservice.model.Role;
import com.ecore.rolesservice.model.dto.membership.MembershipResponseBody;
import com.ecore.rolesservice.model.dto.role.RoleRequestBody;
import com.ecore.rolesservice.model.dto.role.RoleResponseBody;
import com.ecore.rolesservice.service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolesControllerTest {

    @InjectMocks
    private RolesController controller;

    @Mock
    private RoleService roleService;
    @Mock
    private RoleMapper roleMapper;
    @Mock
    private MembershipMapper membershipMapper;

    @Test
    void createRole() {
        final var roleName = TestDataGen.getString();
        final var role = TestDataGen.getObject(Role.class);
        final var roleResponseBody = TestDataGen.getObject(RoleResponseBody.class);

        when(roleService.createRole(anyString())).thenReturn(role);
        when(roleMapper.toRoleResponseBody(any(Role.class))).thenReturn(roleResponseBody);

        var response = controller.createRole(RoleRequestBody.builder().name(roleName).build());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(roleResponseBody);

        verify(roleService).createRole(roleName);
        verify(roleMapper).toRoleResponseBody(role);
        verifyNoInteractions(membershipMapper);
    }

    @Test
    @SuppressWarnings("unchecked")
    void listRoles() {
        final var roles = TestDataGen.getListOfObject(Role.class);
        final var roleResponseList = TestDataGen.getListOfObject(RoleResponseBody.class);

        when(roleService.listRoles()).thenReturn(roles);
        when(roleMapper.toRoleResponseBodyList(any(List.class))).thenReturn(roleResponseList);

        var response = controller.listRoles();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(roleResponseList);

        verify(roleService).listRoles();
        verify(roleMapper).toRoleResponseBodyList(roles);
        verifyNoInteractions(membershipMapper);
    }

    @Test
    @SuppressWarnings("unchecked")
    void listMembersWithRole() {
        final var roleName = TestDataGen.getString();
        final var memberships = TestDataGen.getListOfObject(Membership.class);
        final var membershipResponseList = TestDataGen.getListOfObject(MembershipResponseBody.class);

        when(roleService.findMemberships(anyString())).thenReturn(memberships);
        when(membershipMapper.toMembershipResponseBodyList(any(List.class))).thenReturn(membershipResponseList);

        var response = controller.listMembersWithRole(roleName);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(membershipResponseList);

        verify(roleService).findMemberships(roleName);
        verify(membershipMapper).toMembershipResponseBodyList(memberships);
        verifyNoInteractions(roleMapper);
    }
}
