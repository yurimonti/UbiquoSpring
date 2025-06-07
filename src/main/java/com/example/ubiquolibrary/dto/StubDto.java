package com.example.ubiquolibrary.dto;

import com.example.ubiquolibrary.model.UbiquoRequest;
import com.example.ubiquolibrary.model.UbiquoResponse;

public record StubDto(String host, UbiquoRequest request, UbiquoResponse response, String name) {
}
