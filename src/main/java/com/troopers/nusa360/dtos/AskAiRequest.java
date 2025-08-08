package com.troopers.nusa360.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AskAiRequest {

    @NotBlank(message = "Query is required")
    private String query;

    @NotBlank(message = "Context is required")
    private String context;
}
