package integration.com.ecore.rolesservice.controller.v1;

import com.ecore.rolesservice.AppConstants;
import com.ecore.rolesservice.TestDataGen;
import com.ecore.rolesservice.model.Membership;
import com.ecore.rolesservice.model.MembershipId;
import com.ecore.rolesservice.model.Role;
import com.ecore.rolesservice.model.dto.ErrorDto;
import com.ecore.rolesservice.model.dto.membership.MembershipResponseBody;
import com.ecore.rolesservice.repository.MembershipRepository;
import com.ecore.rolesservice.repository.RoleRepository;
import integration.AbstractIT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MembershipsControllerIntegrationIT extends AbstractIT {

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    MembershipRepository membershipRepository;

    @AfterEach
    void tearDown() {
        membershipRepository.deleteAll();
    }

    @Test
    void assignRole_whenNotATeamMember_thenReturn400() {
        String teamId = "8a49d79b-9885-434b-a636-8322b1eb4367";
        String userId = "ca0c0859-0298-4b37-afe4-df50208cf4c3";
        String url = "/v1/memberships/" + String.join("/", teamId, userId, "assign", "roleName");


        ResponseEntity<ErrorDto> response = restTemplate.postForEntity(url, null, ErrorDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("User is not a team member");

        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors().size()).isEqualTo(1);
        assertThat(response.getBody().getErrors().get(0)).contains(teamId).contains(userId);
    }

    @Test
    void assignRole_whenRoleDoesntExist_thenReturn400() {
        String teamId = "8a49d79b-9885-434b-a636-8322b1eb4367";
        String userId = "ca0c0859-0298-4b37-afe4-df50208cf4c2";
        String roleName = TestDataGen.getString();
        String url = "/v1/memberships/" + String.join("/", teamId, userId, "assign", roleName);


        ResponseEntity<ErrorDto> response = restTemplate.postForEntity(url, null, ErrorDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Cannot to find role");

        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors().size()).isEqualTo(1);
        assertThat(response.getBody().getErrors().get(0)).contains(roleName);
    }

    @Test
    void assignRole_whenIsMemberAndRoleExist_thenReturnMembership() {
        String teamId = "8a49d79b-9885-434b-a636-8322b1eb4367";
        String userId = "ca0c0859-0298-4b37-afe4-df50208cf4c2";
        String roleName = TestDataGen.getString();
        roleRepository.save(Role.builder().name(roleName).build());

        String url = "/v1/memberships/" + String.join("/", teamId, userId, "assign", roleName);

        ResponseEntity<MembershipResponseBody> response = restTemplate.postForEntity(url, null, MembershipResponseBody.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getRole()).isEqualTo(roleName);
        assertThat(response.getBody().getTeamId()).isEqualTo(teamId);
        assertThat(response.getBody().getUserId()).isEqualTo(userId);

        assertThat(membershipRepository.findById(new MembershipId(teamId, userId))).isPresent();
    }

    @Test
    void verifyRoleForMembership_whenIsNotMemberAndIsNotDefaultRole_thenNoContent() {
        String teamId = TestDataGen.getString();
        String userId = TestDataGen.getString();
        String roleName = TestDataGen.getString();

        String url = "/v1/memberships/" + String.join("/", teamId, userId, roleName);

        ResponseEntity<MembershipResponseBody> response = restTemplate.getForEntity(url, MembershipResponseBody.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void verifyRoleForMembership_whenIsNotMemberAndIsDefaultRole_thenReturnDefaultMembership() {
        String teamId = TestDataGen.getString();
        String userId = TestDataGen.getString();
        String roleName = AppConstants.DEFAULT_ROLE_NAME;

        String url = "/v1/memberships/" + String.join("/", teamId, userId, roleName);

        ResponseEntity<MembershipResponseBody> response = restTemplate.getForEntity(url, MembershipResponseBody.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getRole()).isEqualTo(roleName);
        assertThat(response.getBody().getTeamId()).isEqualTo(teamId);
        assertThat(response.getBody().getUserId()).isEqualTo(userId);

        assertThat(membershipRepository.findById(new MembershipId(teamId, userId))).isEmpty();
    }

    @Test
    void verifyRoleForMembership_whenIsMember_thenReturnMembership() {
        String teamId = TestDataGen.getString();
        String userId = TestDataGen.getString();
        String roleName = TestDataGen.getString();
        createRoleAndMembership(teamId, userId, roleName);

        String url = "/v1/memberships/" + String.join("/", teamId, userId, roleName);

        ResponseEntity<MembershipResponseBody> response = restTemplate.getForEntity(url, MembershipResponseBody.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getRole()).isEqualTo(roleName);
        assertThat(response.getBody().getTeamId()).isEqualTo(teamId);
        assertThat(response.getBody().getUserId()).isEqualTo(userId);
    }

    @Test
    void findMembership_whenMembershipDoesntExist_thenReturnDefaultMembership(){
        String teamId = TestDataGen.getString();
        String userId = TestDataGen.getString();
        String roleName = AppConstants.DEFAULT_ROLE_NAME;

        String url = "/v1/memberships/" + String.join("/", teamId, userId);

        ResponseEntity<MembershipResponseBody> response = restTemplate.getForEntity(url, MembershipResponseBody.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getRole()).isEqualTo(roleName);
        assertThat(response.getBody().getTeamId()).isEqualTo(teamId);
        assertThat(response.getBody().getUserId()).isEqualTo(userId);

        assertThat(membershipRepository.findById(new MembershipId(teamId, userId))).isEmpty();
    }

    @Test
    void findMembership_whenMembershipExist_thenReturnMembership(){
        String teamId = TestDataGen.getString();
        String userId = TestDataGen.getString();
        String roleName = TestDataGen.getString();
        createRoleAndMembership(teamId, userId, roleName);


        String url = "/v1/memberships/" + String.join("/", teamId, userId);

        ResponseEntity<MembershipResponseBody> response = restTemplate.getForEntity(url, MembershipResponseBody.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getRole()).isEqualTo(roleName);
        assertThat(response.getBody().getTeamId()).isEqualTo(teamId);
        assertThat(response.getBody().getUserId()).isEqualTo(userId);

        assertThat(membershipRepository.findById(new MembershipId(teamId, userId))).isPresent();
    }

    private void createRoleAndMembership(String teamId, String userId, String roleName) {
        Role role = roleRepository.save(Role.builder().name(roleName).build());
        membershipRepository.save(new Membership(teamId, userId, role));
    }


}
