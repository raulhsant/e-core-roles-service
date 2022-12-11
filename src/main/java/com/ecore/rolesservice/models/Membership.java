package com.ecore.rolesservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import static com.ecore.rolesservice.AppConstants.DEFAULT_ROLE_NAME;

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
