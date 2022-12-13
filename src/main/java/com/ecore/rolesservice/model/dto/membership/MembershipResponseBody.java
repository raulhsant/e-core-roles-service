package com.ecore.rolesservice.model.dto.membership;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipResponseBody {
    private String teamId;
    private String userId;
    private String role;
}
