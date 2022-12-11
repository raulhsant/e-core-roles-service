package com.ecore.rolesservice.exception;

public class UserIsNotTeamMemberException extends HttpException {

    public UserIsNotTeamMemberException(String userId, String teamId) {
        super("User is not a team member");
        error = String.format("User %s is not a member of team %s", userId, teamId);
    }
}
