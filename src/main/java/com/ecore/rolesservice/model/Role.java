
package com.ecore.rolesservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role {
    @Id
    private String name;

    @OneToMany(mappedBy = "role")
    @Builder.Default
    private List<Membership> memberships = new ArrayList<>();
    private OffsetDateTime createdAt;
}

