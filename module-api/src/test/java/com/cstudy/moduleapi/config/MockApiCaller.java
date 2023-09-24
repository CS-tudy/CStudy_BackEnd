package com.cstudy.moduleapi.config;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static com.cstudy.moduleapi.config.ControllerTest.ADMIN_USER;

public abstract class MockApiCaller {

    protected final MockMvc mockMvc;

    protected final ObjectMapper objectMapper;

    public MockApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public ApiResponse<ErrorResponse> sendPostRequestAndParseErrorResponse(String url, Object request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.code"))
                .message(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.message"))
                .validation(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.validation"))
                .build();

        return new ApiResponse<>(response.getStatus(), errorResponse);
    }


    public ApiResponse<ErrorResponse> sendGetRequest_WithNoAuthorization_ExpectErrorResponse(String url, Object request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.code"))
                .message(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.message"))
                .build();

        return new ApiResponse<>(response.getStatus(), errorResponse);
    }

    public ApiResponse<ErrorResponse> sendGetRequest_ExpectErrorResponse(String url, Object request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_USER)
                .content(objectMapper.writeValueAsString(request));

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.code"))
                .message(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.message"))
                .build();

        return new ApiResponse<>(response.getStatus(), errorResponse);
    }

    //---------------------- PATCH ------------------------------------------------------------------------- //
    public ApiResponse<ErrorResponse> sendPatchRequest_ExpectErrorResponse(String url, Object request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_USER)
                .content(objectMapper.writeValueAsString(request));

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.code"))
                .message(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.message"))
                .build();

        return new ApiResponse<>(response.getStatus(), errorResponse);
    }

    public ApiResponse<ErrorResponse> sendPatchRequest_WithNoAuthorization_ExpectErrorResponse(String url, Object request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.code"))
                .message(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.message"))
                .build();

        return new ApiResponse<>(response.getStatus(), errorResponse);
    }
}
