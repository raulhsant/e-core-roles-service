package com.ecore.rolesservice.service;

import com.ecore.rolesservice.models.dto.team.TeamResponseBody;
import com.ecore.rolesservice.utils.httpbodyhanlders.JsonBodyHandler;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Supplier;

@Service
public class TeamsService {

    private final HttpClient httpClient;
    @Value("${variables.teams.url}")
    private String url;

    public TeamsService() {
        httpClient = HttpClient.newHttpClient();
    }

    public boolean isNotTeamMember(String userId, String teamId) {
        var team = this.getTeam(teamId);
        return !team.getTeamMemberIds().contains(userId);
    }

    @SneakyThrows
    private TeamResponseBody getTeam(String teamId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .path("/{teamId}")
                .build(teamId);

        var request = HttpRequest.newBuilder(uri).GET().build();

        HttpResponse<Supplier<TeamResponseBody>> response = httpClient.send(request, new JsonBodyHandler<>(TeamResponseBody.class));

        return isSuccess(response) ? response.body().get() : new TeamResponseBody();
    }

    private boolean isSuccess(HttpResponse response) {
        return response.statusCode() >= 200 && response.statusCode() < 300;
    }
}
