package com.furnistyle.furniturebackend.dtos.responses;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
    private String message;
    private int status;
    private Map<String, String> errors;
}
