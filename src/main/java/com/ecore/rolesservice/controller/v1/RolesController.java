package com.ecore.rolesservice.controller.v1;

import com.ecore.rolesservice.mapper.MembershipMapper;
import com.ecore.rolesservice.mapper.RoleMapper;
import com.ecore.rolesservice.models.Membership;
import com.ecore.rolesservice.models.dto.membership.MembershipResponseBody;
import com.ecore.rolesservice.models.dto.role.RoleRequestBody;
import com.ecore.rolesservice.models.dto.role.RoleResponseBody;
import com.ecore.rolesservice.service.RolesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/roles")
@Api(value = "roles", tags = "roles")
@Tag(name = "roles", description = "Roles")
@RequiredArgsConstructor
public class RolesController {

    private final RolesService rolesService;
    private final RoleMapper roleMapper;
    private final MembershipMapper membershipMapper;

    @ApiOperation(value = "Add a new role")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleResponseBody> createRole(@RequestBody @Valid RoleRequestBody requestBody) {
        var role = rolesService.createRole(requestBody);
        return ResponseEntity.ok(roleMapper.toRoleResponseBody(role));
    }

    @ApiOperation(value = "List existing roles")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleResponseBody>> listRoles() {
        var roles = rolesService.listRoles();
        return ResponseEntity.ok(roleMapper.toRoleResponseBody(roles));
    }

    @ApiOperation(value = "List memberships for role")
    @GetMapping(path = "/{role}/memberships", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MembershipResponseBody>> listMembersWithRole(@PathVariable("role") String roleName) {
        List<Membership> memberships = rolesService.findMemberships(roleName);
        return ResponseEntity.ok(membershipMapper.toMembershipResponseBody(memberships));
    }

}