package com.ecore.rolesservice.mapper;

import com.ecore.rolesservice.model.Role;
import com.ecore.rolesservice.model.dto.role.RoleResponseBody;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "jsr330")
public interface RoleMapper {

    RoleResponseBody toRoleResponseBody(Role role);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<RoleResponseBody> toRoleResponseBodyList(List<Role> roles);
}
