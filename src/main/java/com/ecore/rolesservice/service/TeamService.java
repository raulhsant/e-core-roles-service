package com.ecore.rolesservice.service;

import com.ecore.rolesservice.model.dto.team.TeamResponseBody;
import com.ecore.rolesservice.utils.httpbodyhanlders.JsonBodyHandler;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Supplier;

@Service
public class TeamService {

    private final HttpClient httpClient;
    @Value("${variables.teams.url}")
    private String url;

    public TeamService() {
        httpClient = HttpClient.newHttpClient();
    }


    @SneakyThrows
    public boolean isNotTeamMember(String userId, String teamId) {
        TeamResponseBody team = this.getTeam(teamId);
        return !team.getTeamMemberIds().contains(userId);
    }

    private TeamResponseBody getTeam(String teamId) throws IOException, InterruptedException {
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .path("/{teamId}")
                .build(teamId);

        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();

        HttpResponse<Supplier<TeamResponseBody>> response = httpClient.send(request, new JsonBodyHandler<>(TeamResponseBody.class));
        return getTeamResponseBody(response);
    }

    private TeamResponseBody getTeamResponseBody(HttpResponse<Supplier<TeamResponseBody>> response) {
        if (isSuccess(response)) {
            TeamResponseBody responseBody = response.body().get();
            // This is necessary as the response when a team does not exist is a 200 with body "null"
            return responseBody != null ? responseBody : new TeamResponseBody();
        }
        return new TeamResponseBody();
    }

    private boolean isSuccess(HttpResponse response) {
        return HttpStatus.valueOf(response.statusCode())
                .is2xxSuccessful();
    }
}
