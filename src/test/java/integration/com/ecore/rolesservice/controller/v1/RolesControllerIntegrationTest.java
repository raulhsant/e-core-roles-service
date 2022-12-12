package integration.com.ecore.rolesservice.controller.v1;

import com.ecore.rolesservice.TestDataGen;
import com.ecore.rolesservice.model.Role;
import com.ecore.rolesservice.model.dto.role.RoleResponseBody;
import com.ecore.rolesservice.repository.RoleRepository;
import integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RolesControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    RoleRepository roleRepository;

    @Test
    void listRoles() {
        Role role = Role.builder().name(TestDataGen.getString()).build();
        roleRepository.save(role);

        ResponseEntity<RoleResponseBody[]> response = restTemplate.getForEntity("/v1/roles", RoleResponseBody[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().hasSize(1);

        List<RoleResponseBody> responseBodyList = List.of(response.getBody());
        assertThat(responseBodyList.get(0).getName()).isEqualTo(role.getName());
    }
}
