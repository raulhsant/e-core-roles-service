package com.ecore.rolesservice.model.dto;

import lombok.*;

import java.util.List;

@Builder
@RequiredArgsConstructor
@Getter
public class ErrorDto {
//    private final HttpStatus status;
    private final String message;
    private final List<String> errors;
}
