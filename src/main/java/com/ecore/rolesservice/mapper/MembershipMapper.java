package com.ecore.rolesservice.mapper;

import com.ecore.rolesservice.AppConstants;
import com.ecore.rolesservice.models.Membership;
import com.ecore.rolesservice.models.dto.membership.MembershipResponseBody;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jsr330")
public interface MembershipMapper {

    @Mapping(target = "role", source = "role.name", defaultValue = AppConstants.DEFAULT_ROLE_NAME)
    MembershipResponseBody toMembershipResponseBody(Membership membership);

    List<MembershipResponseBody> toMembershipResponseBody(List<Membership> memberships);
}
