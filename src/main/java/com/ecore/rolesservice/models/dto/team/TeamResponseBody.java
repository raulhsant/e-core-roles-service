package com.ecore.rolesservice.models.dto.team;

import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class TeamResponseBody {
    private Set<String> teamMemberIds;
    private String name;
    private String teamLeadId;
    private String id;
}