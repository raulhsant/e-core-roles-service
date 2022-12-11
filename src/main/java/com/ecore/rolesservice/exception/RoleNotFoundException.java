package com.ecore.rolesservice.exception;

public class RoleNotFoundException extends HttpException {

    public RoleNotFoundException(String roleName) {
        super("Cannot to find role");
        error = String.format("Role %s not found", roleName);
    }
}
