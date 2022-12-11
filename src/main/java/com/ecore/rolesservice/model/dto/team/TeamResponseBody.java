package com.ecore.rolesservice.model.dto.team;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponseBody {
    private Set<String> teamMemberIds = new HashSet<>();
    private String name;
    private String teamLeadId;
    private String id;
}