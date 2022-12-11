package com.ecore.rolesservice.service;

import com.ecore.rolesservice.model.dto.team.TeamResponseBody;
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
public class TeamService {

    private final HttpClient httpClient;
    @Value("${variables.teams.url}")
    private String url;

    public TeamService() {
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
        return getTeamResponseBody(response);
    }

    private TeamResponseBody getTeamResponseBody(HttpResponse<Supplier<TeamResponseBody>> response) {
        if(isSuccess(response)){
            TeamResponseBody responseBody = response.body().get();
            // This is necessary as the response when a team does not exist is a 200 with body "null"
            return responseBody != null ? responseBody :  new TeamResponseBody();
        }
        return new TeamResponseBody();
    }

    private boolean isSuccess(HttpResponse response) {
        return response.statusCode() >= 200 && response.statusCode() < 300;
    }
}
