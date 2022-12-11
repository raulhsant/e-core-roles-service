package com.ecore.rolesservice.models.dto.membership;

import lombok.Data;

@Data
public class MembershipResponseBody {

    private String teamId;
    private String userId;
    private String role;
}
