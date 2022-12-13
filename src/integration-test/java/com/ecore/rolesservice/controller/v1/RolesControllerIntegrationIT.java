package integration.com.ecore.rolesservice.controller.v1;

import com.ecore.rolesservice.TestDataGen;
import com.ecore.rolesservice.model.Membership;
import com.ecore.rolesservice.model.Role;
import com.ecore.rolesservice.model.dto.ErrorDto;
import com.ecore.rolesservice.model.dto.membership.MembershipResponseBody;
import com.ecore.rolesservice.model.dto.role.RoleRequestBody;
import com.ecore.rolesservice.model.dto.role.RoleResponseBody;
import com.ecore.rolesservice.repository.MembershipRepository;
import com.ecore.rolesservice.repository.RoleRepository;
import integration.AbstractIT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RolesControllerIntegrationIT extends AbstractIT {

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    MembershipRepository membershipRepository;

    @AfterEach
    void tearDown() {
        membershipRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void createRole_whenInvalidRequest_thenReturn400() {
        RoleRequestBody roleRequestBody = new RoleRequestBody();
        ResponseEntity<ErrorDto> response = restTemplate.postForEntity("/v1/roles", roleRequestBody, ErrorDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Found 1 errors on request");

        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors().size()).isEqualTo(1);
        assertThat(response.getBody().getErrors().get(0)).contains("name:");
    }

    @Test
    void createRole_whenDoesNotExist_thenCreate() {
        OffsetDateTime testStart = OffsetDateTime.now();
        RoleRequestBody roleRequestBody = new RoleRequestBody(TestDataGen.getString());
        ResponseEntity<RoleResponseBody> response = restTemplate.postForEntity("/v1/roles", roleRequestBody, RoleResponseBody.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(roleRequestBody.getName());
        assertThat(response.getBody().getCreatedAt()).isAfter(testStart);
    }

    @Test
    void createRole_whenRoleExist_thenReturnExistingRole() {
        String roleName = TestDataGen.getString();
        Role role = roleRepository.save(Role.builder().name(roleName).createdAt(OffsetDateTime.now()).build());

        RoleRequestBody roleRequestBody = new RoleRequestBody(roleName);
        ResponseEntity<RoleResponseBody> response = restTemplate.postForEntity("/v1/roles", roleRequestBody, RoleResponseBody.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(roleName);
        assertThat(response.getBody().getCreatedAt()).isEqualTo(role.getCreatedAt().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void listRoles_whenHasRole_thenReturnPopulatedList() {
        Role role = roleRepository.save(Role.builder().name(TestDataGen.getString()).createdAt(OffsetDateTime.now()).build());

        ResponseEntity<RoleResponseBody[]> response = restTemplate.getForEntity("/v1/roles", RoleResponseBody[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().hasSize(1);

        List<RoleResponseBody> responseBodyList = List.of(response.getBody());
        assertThat(responseBodyList.get(0).getName()).isEqualTo(role.getName());
        assertThat(responseBodyList.get(0).getCreatedAt()).isEqualTo(role.getCreatedAt().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void listRoles_whenIsEmpty_thenReturnEmptyList() {
        ResponseEntity<RoleResponseBody[]> response = restTemplate.getForEntity("/v1/roles", RoleResponseBody[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isEmpty();
    }

    @Test
    void listMembersWithRole_whenRoleDoesNotExist_thenReturnEmptyList() {
        ResponseEntity<MembershipResponseBody[]> response = restTemplate.getForEntity("/v1/roles/roleName/memberships",
                MembershipResponseBody[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isEmpty();
    }

    @Test
    void listMembersWithRole_whenRoleHasNoMemberships_thenReturnEmptyList() {
        roleRepository.save(Role.builder().name("roleName").createdAt(OffsetDateTime.now()).build());
        ResponseEntity<MembershipResponseBody[]> response = restTemplate.getForEntity("/v1/roles/roleName/memberships",
                MembershipResponseBody[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isEmpty();
    }

    @Test
    void listMembersWithRole_whenRoleHasMemberships_thenReturnMembershipList() {
        Role role = roleRepository.save(Role.builder().name("roleName").createdAt(OffsetDateTime.now()).build());
        List<Membership> memberships = TestDataGen.getListOfObject(Membership.class);
        memberships.forEach(m -> m.setRole(role));
        membershipRepository.saveAll(memberships);

        final var expectedMembershipResponses = memberships.stream()
                .map(m -> new MembershipResponseBody(m.getTeamId(), m.getUserId(), m.getRole().getName()))
                .collect(Collectors.toSet());

        ResponseEntity<MembershipResponseBody[]> response = restTemplate.getForEntity("/v1/roles/roleName/memberships", MembershipResponseBody[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().hasSize(expectedMembershipResponses.size());
        assertThat(Set.of(response.getBody())).isEqualTo(expectedMembershipResponses);
    }
}
