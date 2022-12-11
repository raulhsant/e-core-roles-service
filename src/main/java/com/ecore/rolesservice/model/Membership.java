package com.ecore.rolesservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(MembershipId.class)
public class Membership {
    @Id
    private String teamId;
    @Id
    private String userId;

    @ManyToOne
    private Role role;
}
