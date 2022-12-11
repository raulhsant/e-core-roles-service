package com.ecore.rolesservice.model.dto.role;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class RoleResponseBody extends RoleRequestBody {
    private OffsetDateTime createdAt;
}
