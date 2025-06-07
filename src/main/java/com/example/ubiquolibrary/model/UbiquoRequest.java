package com.example.ubiquolibrary.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
@AllArgsConstructor
@Builder
public class UbiquoRequest {
    private String method;
    private String uri;
    @Nullable
    private Map<String, List<String>> headers;
    @Nullable
    private JsonNode body;
}
