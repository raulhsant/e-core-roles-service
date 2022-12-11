package com.ecore.rolesservice.service;

import com.ecore.rolesservice.TestDataGen;
import com.ecore.rolesservice.exception.RoleNotFoundException;
import com.ecore.rolesservice.exception.UserIsNotTeamMemberException;
import com.ecore.rolesservice.model.Membership;
import com.ecore.rolesservice.model.MembershipId;
import com.ecore.rolesservice.model.Role;
import com.ecore.rolesservice.repository.MembershipRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.ecore.rolesservice.AppConstants.DEFAULT_ROLE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class MembershipServiceTest {

    @InjectMocks
    private MembershipService service;
    @Mock
    private TeamService teamService;
    @Mock
    private RoleService rolesService;
    @Mock
    private MembershipRepository membershipRepository;

    @Test
    void assignRole_whenUserIsNotATeamMember_thenThrowException() {
        final var teamId = TestDataGen.getString();
        final var userId = TestDataGen.getString();

        when(teamService.isNotTeamMember(anyString(), anyString())).thenReturn(Boolean.TRUE);

        final var exception = assertThrows(UserIsNotTeamMemberException.class, () -> service.assignRole(null, teamId, userId));

        assertThat(exception.getError()).contains(userId).contains(teamId);

        verify(teamService).isNotTeamMember(userId, teamId);
        verifyNoInteractions(rolesService);
        verifyNoInteractions(membershipRepository);
    }

    @Test
    void assignRole_whenRoleNotFound_thenThrowException() {
        final var roleName = TestDataGen.getString();
        final var teamId = TestDataGen.getString();
        final var userId = TestDataGen.getString();

        when(teamService.isNotTeamMember(anyString(), anyString())).thenReturn(Boolean.FALSE);
        when(rolesService.getById(anyString())).thenReturn(Optional.empty());

        final var exception = assertThrows(RoleNotFoundException.class, () -> service.assignRole(roleName, teamId, userId));

        assertThat(exception.getError()).contains(roleName);

        verify(teamService).isNotTeamMember(userId, teamId);
        verify(rolesService).getById(roleName);
        verifyNoInteractions(membershipRepository);
    }

    @Test
    void assignRole_whenUserISTeamMemberAndRoleExists_thenAssignRoleAndReturn() {
        final var roleName = TestDataGen.getString();
        final var teamId = TestDataGen.getString();
        final var userId = TestDataGen.getString();

        final var role = TestDataGen.getObject(Role.class);
        final var membership = Membership.builder()
                .teamId(teamId)
                .userId(userId)
                .role(role)
                .build();


        when(teamService.isNotTeamMember(anyString(), anyString())).thenReturn(Boolean.FALSE);
        when(rolesService.getById(anyString())).thenReturn(Optional.of(role));
        when(membershipRepository.save(any(Membership.class))).thenReturn(membership);

        final var response = service.assignRole(roleName, teamId, userId);

        assertThat(response).isEqualTo(membership);

        verify(teamService).isNotTeamMember(userId, teamId);
        verify(rolesService).getById(roleName);
        verify(membershipRepository).save(membership);
    }

    @Test
    void checkRoleAssigment_whenIsNotMemberAndNotDefaultRole_thenReturnEmpty() {
        final var roleName = TestDataGen.getString();
        final var teamId = TestDataGen.getString();
        final var userId = TestDataGen.getString();

        when(membershipRepository.findByteamIdAndUserIdAndRole(anyString(), anyString(), any(Role.class))).thenReturn(Optional.empty());

        final var response = service.checkRoleAssigment(roleName, teamId, userId);

        assertThat(response).isEmpty();

        verify(membershipRepository).findByteamIdAndUserIdAndRole(teamId, userId, Role.builder().name(roleName).build());
        verifyNoInteractions(teamService);
        verifyNoInteractions(rolesService);
    }

    @Test
    void checkRoleAssigment_whenIsNotMemberAndIsDefaultRole_thenReturnBaseMembership() {
        final var roleName = DEFAULT_ROLE_NAME;
        final var teamId = TestDataGen.getString();
        final var userId = TestDataGen.getString();

        when(membershipRepository.findByteamIdAndUserIdAndRole(anyString(), anyString(), any(Role.class))).thenReturn(Optional.empty());

        final var response = service.checkRoleAssigment(roleName, teamId, userId);

        assertThat(response).isPresent();
        assertThat(response.get().getTeamId()).isEqualTo(teamId);
        assertThat(response.get().getUserId()).isEqualTo(userId);
        assertThat(response.get().getRole()).isNull();

        verify(membershipRepository).findByteamIdAndUserIdAndRole(teamId, userId, Role.builder().name(roleName).build());
        verifyNoInteractions(teamService);
        verifyNoInteractions(rolesService);
    }

    @Test
    void checkRoleAssigment_whenRoleMember_thenReturnMembership() {
        final var roleName = TestDataGen.getString();
        final var teamId = TestDataGen.getString();
        final var userId = TestDataGen.getString();

        final var membership = Membership.builder()
                .teamId(teamId)
                .userId(userId)
                .role(Role.builder().name(roleName).build())
                .build();

        when(membershipRepository.findByteamIdAndUserIdAndRole(anyString(), anyString(), any(Role.class))).thenReturn(Optional.of(membership));

        final var response = service.checkRoleAssigment(roleName, teamId, userId);

        assertThat(response).isPresent();
        assertThat(response.get().getTeamId()).isEqualTo(teamId);
        assertThat(response.get().getUserId()).isEqualTo(userId);
        assertThat(response.get().getRole().getName()).isEqualTo(roleName);

        verify(membershipRepository).findByteamIdAndUserIdAndRole(teamId, userId, Role.builder().name(roleName).build());
        verifyNoInteractions(teamService);
        verifyNoInteractions(rolesService);
    }

    @Test
    void getMembership_whenNotOnRepository_thenReturnBaseMembership() {
        final var teamId = TestDataGen.getString();
        final var userId = TestDataGen.getString();
        final var membershipId = new MembershipId(teamId, userId);

        when(membershipRepository.findById(any(MembershipId.class))).thenReturn(Optional.empty());

        final var response = service.getMembership(teamId, userId);

        assertThat(response.getTeamId()).isEqualTo(teamId);
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getRole()).isNull();

        verify(membershipRepository).findById(membershipId);
        verifyNoInteractions(teamService);
        verifyNoInteractions(rolesService);
    }

    @Test
    void getMembership_whenOnRepository_thenReturnMembership() {
        final var teamId = TestDataGen.getString();
        final var userId = TestDataGen.getString();
        final var membershipId = new MembershipId(teamId, userId);
        final var membership = Membership.builder()
                .teamId(teamId)
                .userId(userId)
                .role(TestDataGen.getObject(Role.class)).build();

        when(membershipRepository.findById(any(MembershipId.class))).thenReturn(Optional.of(membership));

        final var response = service.getMembership(teamId, userId);

        assertThat(response).isEqualTo(membership);

        verify(membershipRepository).findById(membershipId);
        verifyNoInteractions(teamService);
        verifyNoInteractions(rolesService);
    }
}
