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
public class UbiquoResStringBody {
    private int status;
    @Nullable
    private Map<String, List<String>> headers;
    @Nullable
    private Object body;
}
