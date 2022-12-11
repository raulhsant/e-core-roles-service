package com.ecore.rolesservice.service;

import com.ecore.rolesservice.model.Membership;
import com.ecore.rolesservice.model.Role;
import com.ecore.rolesservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role createRole(@NotBlank String roleName) {
        Optional<Role> roleOptional = roleRepository.findById(roleName);

        if (roleOptional.isPresent()) {
            return roleOptional.get();
        }

        var role = Role.builder()
                .name(roleName)
                .createdAt(OffsetDateTime.now())
                .build();

        return roleRepository.save(role);
    }

    public List<Membership> findMemberships(String roleName) {
        Optional<Role> role = roleRepository.findById(roleName);
        return role.orElse(new Role()).getMemberships();
    }

    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getById(String roleName) {
        return roleRepository.findById(roleName);
    }
}
