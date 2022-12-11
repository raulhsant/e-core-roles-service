package com.ecore.rolesservice.models.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Builder
@RequiredArgsConstructor
@Getter
public class ErrorDto {
//    private final HttpStatus status;
    private final String message;
    private final List<String> errors;
}
