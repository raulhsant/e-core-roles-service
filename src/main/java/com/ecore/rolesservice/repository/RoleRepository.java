package com.ecore.rolesservice.repository;

import com.ecore.rolesservice.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {

//    Role save(Role role);
//    Optional<Role> search(String name);
//    void assing(String roleName, String teamId, String userId);

//    List<Membership> findMemberships(String roleName);

//    boolean isAssigned(String roleName, String teamId, String userId);
}
