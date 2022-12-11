package com.ecore.rolesservice.controller.v1;


import com.ecore.rolesservice.mapper.MembershipMapper;
import com.ecore.rolesservice.model.dto.membership.MembershipResponseBody;
import com.ecore.rolesservice.service.MembershipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/v1/membership")
@Api(value = "memberships", tags = "memberships")
@Tag(name = "memberships", description = "Memberships")
@RequiredArgsConstructor
public class MembershipsController {

    private final MembershipService membershipService;
    private final MembershipMapper membershipMapper;

    @ApiOperation(value = "Assign role to a membership")
    @PostMapping(path = "/{teamId}/{userId}/assign/{role}", produces = MediaType.APPLICATION_JSON_VALUE)
    //todo: AssignRoleResponseBody?
    public ResponseEntity<MembershipResponseBody> assignRole(@PathVariable("role") String role,
                                                             @PathVariable("teamId") @NotBlank String teamId,
                                                             @PathVariable("userId") @NotBlank String userId) {
        var membership = membershipService.assignRole(role, teamId, userId);
        return ResponseEntity.ok(membershipMapper.toMembershipResponseBody(membership));
    }

    //todo: avaliar migrar isso aqui...
    @ApiOperation(value = "Check if membership has role")
    @GetMapping(path = "/{teamId}/{userId}/isMember/{role}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MembershipResponseBody> findRoleForMembership(@PathVariable("role") @NotBlank String roleName,
                                                                        @PathVariable("teamId") @NotBlank String teamId,
                                                                        @PathVariable("userId") @NotBlank String userId) {
        return membershipService.checkRoleAssigment(roleName, teamId, userId)
                .map(membershipMapper::toMembershipResponseBody)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @ApiOperation(value = "Return current role for membership")
    @GetMapping(path = "/{teamId}/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MembershipResponseBody> findRoleForMembership(@PathVariable("teamId") @NotBlank String teamId,
                                                                        @PathVariable("userId") @NotBlank String userId) {
        var membership = membershipService.getMembership(teamId, userId);
        return ResponseEntity.ok(membershipMapper.toMembershipResponseBody(membership));
    }
}
