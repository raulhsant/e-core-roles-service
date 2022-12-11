package com.ecore.rolesservice.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class MembershipId implements Serializable {
    String teamId;
    String userId;
}
