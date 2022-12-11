package com.ecore.rolesservice.service;

import com.ecore.rolesservice.TestDataGen;
import com.ecore.rolesservice.model.Role;
import com.ecore.rolesservice.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @InjectMocks
    private RoleService service;

    @Mock
    private RoleRepository roleRepository;

    @Test
    void createRole_whenRoleAlreadyExists_thenReturnExistingRole() {
        final var roleName = TestDataGen.getString();
        final var role = TestDataGen.getObject(Role.class);

        when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));

        final var response = service.createRole(roleName);

        assertThat(response).isEqualTo(role);

        verify(roleRepository).findById(roleName);
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    void createRole_whenRoleNotExists_thenCreateRole() {
        final var roleName = TestDataGen.getString();

        when(roleRepository.findById(anyString())).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenAnswer(i -> i.getArgument(0));

        final var response = service.createRole(roleName);

        assertThat(response.getName()).isEqualTo(roleName);
        assertThat(response.getCreatedAt()).isBeforeOrEqualTo(OffsetDateTime.now());
        assertThat(response.getMemberships()).isEmpty();

        verify(roleRepository).findById(roleName);
        verify(roleRepository).save(response);
    }

    @Test
    void findMemberships_whenRoleDoesNotExist_thenReturnEmptyList() {
        final var roleName = TestDataGen.getString();

        when(roleRepository.findById(anyString())).thenReturn(Optional.empty());

        final var response = service.findMemberships(roleName);

        assertThat(response).isEmpty();

        verify(roleRepository).findById(roleName);
    }

    @Test
    void findMemberships_whenRoleExists_thenReturnMemberships() {
        final var roleName = TestDataGen.getString();
        final var role = TestDataGen.getObject(Role.class);

        when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));

        final var response = service.findMemberships(roleName);

        assertThat(response).isEqualTo(role.getMemberships());

        verify(roleRepository).findById(roleName);
    }


    @Test
    void listRoles() {
        final var roles = TestDataGen.getListOfObject(Role.class);

        when(roleRepository.findAll()).thenReturn(roles);

        final var response = service.listRoles();

        assertThat(response).isEqualTo(roles);

        verify(roleRepository).findAll();
    }

    @Test
    void getById_whenNotExists_theReturnEmpty() {
        final var roleName = TestDataGen.getString();

        when(roleRepository.findById(anyString())).thenReturn(Optional.empty());

        final var response = service.getById(roleName);

        assertThat(response).isEmpty();

        verify(roleRepository).findById(roleName);
    }

    @Test
    void getById_whenExists_theReturnRole() {
        final var roleName = TestDataGen.getString();
        final var role = TestDataGen.getObject(Role.class);

        when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));

        final var response = service.getById(roleName);

        assertThat(response).isPresent().contains(role);

        verify(roleRepository).findById(roleName);
    }
}
