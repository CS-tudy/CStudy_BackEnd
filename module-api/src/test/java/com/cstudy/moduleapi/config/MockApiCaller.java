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
import static com.cstudy.moduleapi.config.ControllerTest.CUSTOM_USER;

public abstract class MockApiCaller {

    public final MockMvc mockMvc;

    public final ObjectMapper objectMapper;

    public MockApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public ApiResponse<ErrorResponse> sendPostRequest_WithAuthorization_ParseErrorResponse(String url, Object request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_USER)
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

    public ApiResponse<ErrorResponse> sendPostRequest_WithAuthorization_USER_ParseErrorResponse(String url, Object request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + CUSTOM_USER)
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

    public ApiResponse<ErrorResponse> sendPostRequest_WithNoAuthorization_ParseErrorResponse(String url, Object request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(url)
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

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.patch(url)
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

    /**
     *
     * @param url 특정 url을 입력
     * @param request requestBody Dto를 입력
     * @return ErrorResponse
     * @throws Exception
     */
    public ApiResponse<ErrorResponse> sendPatchRequest_USER_ExpectErrorResponse(String url, Object request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + CUSTOM_USER)
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

    public ApiResponse<ErrorResponse> sendPatchRequest_WithNoAuthorization_ExpectErrorResponse(String url, Object request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.patch(url)
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


    //---------------------- Delete ------------------------------------------------------------------------- //
    public ApiResponse<ErrorResponse> sendDeleteRequest_ExpectErrorResponse(String url, Object request, Long path) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete(url,path)
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

    public ApiResponse<ErrorResponse> sendDeleteRequest_USER_ExpectErrorResponse(String url, Object request, Long path) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete(url,path)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + CUSTOM_USER)
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

    public ApiResponse<ErrorResponse> sendDeleteRequest_WithNoAuthorization_ExpectErrorResponse(String url, Object request, Long path) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete(url, path)
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
