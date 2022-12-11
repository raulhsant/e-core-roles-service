package com.ecore.rolesservice.service;

import com.ecore.rolesservice.exception.RoleNotFoundException;
import com.ecore.rolesservice.exception.UserIsNotTeamMemberException;
import com.ecore.rolesservice.model.Membership;
import com.ecore.rolesservice.model.MembershipId;
import com.ecore.rolesservice.model.Role;
import com.ecore.rolesservice.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.ecore.rolesservice.AppConstants.DEFAULT_ROLE_NAME;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final TeamService teamService;
    private final RoleService rolesService;
    private final MembershipRepository membershipRepository;

    public Membership assignRole(String roleName, String teamId, String userId) {

        if (teamService.isNotTeamMember(userId, teamId)) {
            throw new UserIsNotTeamMemberException(userId, teamId);
        }

        Optional<Role> roleOptional = rolesService.getById(roleName);

        if (roleOptional.isEmpty()) {
            throw new RoleNotFoundException(roleName);
        }

        Membership membership = Membership.builder()
                .teamId(teamId)
                .userId(userId)
                .role(roleOptional.get())
                .build();

        return membershipRepository.save(membership);
    }

    public Optional<Membership> checkRoleAssigment(String roleName, String teamId, String userId) {
        Role role = Role.builder().name(roleName).build();
        Optional<Membership> membershipOptional = membershipRepository.findByteamIdAndUserIdAndRole(teamId, userId, role);

        if (membershipOptional.isEmpty() && DEFAULT_ROLE_NAME.equals(roleName)) {
            return Optional.of(createBaseMembership(teamId, userId));
        }

        return membershipOptional;
    }

    public Membership getMembership(String teamId, String userId) {
        Optional<Membership> membershipOptional = membershipRepository.findById(new MembershipId(teamId, userId));
        return membershipOptional.orElse(createBaseMembership(teamId, userId));
    }

    private Membership createBaseMembership(String teamId, String userId) {
        return Membership.builder()
                .teamId(teamId)
                .userId(userId)
                .build();
    }
}
