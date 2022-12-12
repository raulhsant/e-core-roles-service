package com.ecore.rolesservice.controller.v1;


import com.ecore.rolesservice.mapper.MembershipMapper;
import com.ecore.rolesservice.model.dto.membership.MembershipResponseBody;
import com.ecore.rolesservice.service.MembershipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/membership")
@Api(value = "memberships", tags = "Memberships")
@Tag(name = "Memberships", description = "Membership Management")
@RequiredArgsConstructor
public class MembershipsController {

    private final MembershipService membershipService;
    private final MembershipMapper membershipMapper;

    @ApiOperation(value = "Assign role to a membership")
    @PostMapping(path = "/{teamId}/{userId}/assign/{role}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MembershipResponseBody> assignRole(@PathVariable("teamId") String teamId,
                                                             @PathVariable("userId") String userId,
                                                             @PathVariable("role") String role) {
        var membership = membershipService.assignRole(role, teamId, userId);
        return ResponseEntity.ok(membershipMapper.toMembershipResponseBody(membership));
    }

    @ApiOperation(value = "Check if membership has role")
    @ApiResponse(responseCode = "204", description = "Not a member")
    @GetMapping(path = "/{teamId}/{userId}/{role}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MembershipResponseBody> verifyRoleForMembership(@PathVariable("teamId") String teamId,
                                                                          @PathVariable("userId") String userId,
                                                                          @PathVariable("role") String roleName) {
        return membershipService.checkRoleAssigment(roleName, teamId, userId)
                .map(membershipMapper::toMembershipResponseBody)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @ApiOperation(value = "Return current role for membership")
    @GetMapping(path = "/{teamId}/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MembershipResponseBody> findRoleForMembership(@PathVariable("teamId") String teamId,
                                                                        @PathVariable("userId") String userId) {
        var membership = membershipService.getMembership(teamId, userId);
        return ResponseEntity.ok(membershipMapper.toMembershipResponseBody(membership));
    }
}
