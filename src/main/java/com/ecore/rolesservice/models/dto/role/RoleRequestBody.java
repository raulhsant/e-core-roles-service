package com.ecore.rolesservice.models.dto.role;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequestBody {
    @NotBlank(message = "Missing required parameter.")
    private String name;
}
