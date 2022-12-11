package com.ecore.rolesservice.mapper;

import com.ecore.rolesservice.AppConstants;
import com.ecore.rolesservice.model.Membership;
import com.ecore.rolesservice.model.Role;
import com.ecore.rolesservice.model.dto.membership.MembershipResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "jsr330")
public interface MembershipMapper {

    MembershipResponseBody toMembershipResponseBody(Membership membership);

    default String getRoleName(Role role){
        if(role == null || StringUtils.isBlank(role.getName())){
            return AppConstants.DEFAULT_ROLE_NAME;
        }

        return role.getName();
    }

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<MembershipResponseBody> toMembershipResponseBodyList(List<Membership> memberships);
}
