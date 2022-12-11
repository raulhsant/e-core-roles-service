package com.ecore.rolesservice.service;

import com.ecore.rolesservice.TestDataGen;
import com.ecore.rolesservice.model.dto.team.TeamResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgssoft.httpclient.HttpClientMock;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    private static final String TEST_URL = "http://testUrl.com";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private TeamService service;
    private HttpClientMock httpClientMock;

    @BeforeEach
    void setUp() {
        service = new TeamService();
        httpClientMock = new HttpClientMock();
        ReflectionTestUtils.setField(service, "url", TEST_URL);
        ReflectionTestUtils.setField(service, "httpClient", httpClientMock);
    }

    @Test
    void isNotTeamMember_whenTeamDoesntExist_thenReturnTrue() {
        final var teamId = TestDataGen.getString();

        httpClientMock.onGet(TEST_URL).withPath("/" + teamId).doReturn(200, "null");

        assertThat(service.isNotTeamMember(null, teamId)).isTrue();

        httpClientMock.verify().get(TEST_URL).withPath("/" + teamId).called();
    }

    @ParameterizedTest
    @EnumSource(value = HttpStatus.class, names = {"CONTINUE", "MULTIPLE_CHOICES", "BAD_REQUEST", "INTERNAL_SERVER_ERROR" })
    void isNotTeamMember_whenTeamReturnsError_thenReturnTrue(HttpStatus status) {
        final var teamId = TestDataGen.getString();

        httpClientMock.onGet(TEST_URL).withPath("/" + teamId).doReturnStatus(status.value());

        assertThat(service.isNotTeamMember(null, teamId)).isTrue();

        httpClientMock.verify().get(TEST_URL).withPath("/" + teamId).called();
    }

    @SneakyThrows
    @Test
    void isNotTeamMember_whenIsNotTeamMember_thenReturnTrue() {
        final var teamId = TestDataGen.getString();
        final var userId = TestDataGen.getString();
        var team = TestDataGen.getObject(TeamResponseBody.class);

        httpClientMock.onGet(TEST_URL).withPath("/" + teamId).doReturn(200, objectMapper.writeValueAsString(team));

        assertThat(service.isNotTeamMember(userId, teamId)).isTrue();

        httpClientMock.verify().get(TEST_URL).withPath("/" + teamId).called();
    }

    @SneakyThrows
    @Test
    void isNotTeamMember_whenIsTeamMember_thenReturnFalse() {
        final var teamId = TestDataGen.getString();
        final var userId = TestDataGen.getString();
        var team = TestDataGen.getObject(TeamResponseBody.class);
        team.getTeamMemberIds().add(userId);

        httpClientMock.onGet(TEST_URL).withPath("/" + teamId).doReturn(200, objectMapper.writeValueAsString(team));

        assertThat(service.isNotTeamMember(null, teamId)).isTrue();

        httpClientMock.verify().get(TEST_URL).withPath("/" + teamId).called();
    }
}
