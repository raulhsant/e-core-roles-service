package com.ecore.rolesservice.repository;

import com.ecore.rolesservice.model.Membership;
import com.ecore.rolesservice.model.MembershipId;
import com.ecore.rolesservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, MembershipId> {

//    Optional<Membership> findByIdAndRole(MembershipId id, Role role);

    Optional<Membership> findByteamIdAndUserIdAndRole(String teamId, String userId, Role role);
}
