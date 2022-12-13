package com.ecore.rolesservice.controller.v1;

import com.ecore.rolesservice.mapper.MembershipMapper;
import com.ecore.rolesservice.mapper.RoleMapper;
import com.ecore.rolesservice.model.Membership;
import com.ecore.rolesservice.model.Role;
import com.ecore.rolesservice.model.dto.membership.MembershipResponseBody;
import com.ecore.rolesservice.model.dto.role.RoleRequestBody;
import com.ecore.rolesservice.model.dto.role.RoleResponseBody;
import com.ecore.rolesservice.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/roles")
@Api(value = "roles", tags = "Roles")
@Tag(name = "Roles", description = "Role Management")
@RequiredArgsConstructor
public class RolesController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;
    private final MembershipMapper membershipMapper;

    @ApiOperation(value = "Add a new role")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleResponseBody> createRole(@RequestBody @Validated RoleRequestBody requestBody) {
        Role role = roleService.createRole(requestBody.getName());
        return ResponseEntity.ok(roleMapper.toRoleResponseBody(role));
    }

    @ApiOperation(value = "List existing roles")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleResponseBody>> listRoles() {
        List<Role> roles = roleService.listRoles();
        return ResponseEntity.ok(roleMapper.toRoleResponseBodyList(roles));
    }

    @ApiOperation(value = "List memberships for role")
    @GetMapping(path = "/{role}/memberships", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MembershipResponseBody>> listMembersWithRole(@PathVariable("role") String roleName) {
        List<Membership> memberships = roleService.findMemberships(roleName);
        return ResponseEntity.ok(membershipMapper.toMembershipResponseBodyList(memberships));
    }

}
