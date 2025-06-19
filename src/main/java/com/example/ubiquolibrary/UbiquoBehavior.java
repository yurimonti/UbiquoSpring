package com.example.ubiquolibrary;

import com.example.ubiquolibrary.dto.AddStubDto;
import com.example.ubiquolibrary.dto.StubDto;
import com.example.ubiquolibrary.exception.StubToAddNotValidException;
import com.example.ubiquolibrary.model.UbiquoReqStringBody;
import com.example.ubiquolibrary.model.UbiquoRequest;
import com.example.ubiquolibrary.model.UbiquoResStringBody;
import com.example.ubiquolibrary.model.UbiquoResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.json.JsonAssert;
import org.springframework.web.client.RestClient;

public class UbiquoBehavior {
    private final String client;
    private final String clientName;
    private final String ubiquo;
    private final String sutName;
    private UbiquoRequest request;
    private UbiquoResponse response;
    private RestClient loadClient;

    public UbiquoBehavior(String client, String clientName, String ubiquo, String sutName) {
        this.client = client;
        this.clientName = clientName;
        this.ubiquo = ubiquo;
        this.sutName = sutName;
        this.loadClient = RestClient.builder().baseUrl(ubiquo+"/api/v2/admin/stubs/sut").build();
        this.request = null;
        this.response = null;
    }
    // Getters, logic, etc.

    public UbiquoBehavior withRequest(UbiquoReqStringBody request){
        this.request = UbiquoRequest.builder()
                .headers(request.getHeaders())
                .uri(request.getUri())
                .method(request.getMethod())
                .build();
        Object reqBody = request.getBody();
        if(reqBody != null){
            try{
                ObjectMapper mapper = new ObjectMapper();
                String stringBody = reqBody instanceof String ? (String)reqBody : mapper.writeValueAsString(reqBody);
                JsonNode actualBody = mapper.readTree(stringBody);
                this.request.setBody(actualBody);
            }
            catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
        return this;
    }
    public UbiquoBehavior withResponse(UbiquoResStringBody response){
        this.response = UbiquoResponse.builder()
                .headers(response.getHeaders())
                .status(response.getStatus())
                .build();
        Object resBody = response.getBody();
        if(resBody != null){
            try{
                ObjectMapper mapper = new ObjectMapper();
                JsonNode actualBody = null;
                if(resBody instanceof String && isValidJson((String)resBody,mapper))
                {
                    actualBody = mapper.readTree((String)resBody);
                }
                else {
                    String stringBody = mapper.writeValueAsString(resBody);
                    actualBody = mapper.readTree(stringBody);
                }
                this.response.setBody(actualBody);
            }
            catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
        return this;
    }

    private boolean isValidJson(String json, ObjectMapper objectMapper) {
        try {
            objectMapper.readTree(json);  // Can parse both objects and arrays
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void load(){
        StubDto stubDto = new StubDto(this.client,this.request,this.response,this.clientName);
        this.request = null;
        this.response = null;
        AddStubDto addStubDto = new AddStubDto(this.sutName, new StubDto[]{stubDto});
        ResponseEntity<String> res = this.loadClient.post().body(addStubDto).retrieve().toEntity(String.class);
        String content = res.getBody();
        if(res.getStatusCode() != HttpStatusCode.valueOf(200)) throw new StubToAddNotValidException(content);
    }
}