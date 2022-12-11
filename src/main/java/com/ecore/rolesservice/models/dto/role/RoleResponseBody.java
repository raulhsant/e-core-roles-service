package com.ecore.rolesservice.models.dto.role;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public class RoleResponseBody extends RoleRequestBody {
}
