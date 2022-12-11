package com.ecore.rolesservice.mapper;

import com.ecore.rolesservice.models.Role;
import com.ecore.rolesservice.models.dto.role.RoleResponseBody;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "jsr330")
public interface RoleMapper {

    RoleResponseBody toRoleResponseBody(Role role);

    List<RoleResponseBody> toRoleResponseBody(List<Role> roles);
}
