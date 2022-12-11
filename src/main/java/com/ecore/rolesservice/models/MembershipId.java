package com.ecore.rolesservice.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class MembershipId implements Serializable {
    String teamId;
    String userId;
}
