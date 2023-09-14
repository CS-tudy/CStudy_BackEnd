package com.cstudy.moduleapi.controller.apiCaller;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.MockApiCaller;
import com.cstudy.moduleapi.dto.member.MemberLoginRequest;
import com.cstudy.moduleapi.dto.member.MemberLoginResponse;
import com.cstudy.moduleapi.dto.member.MemberSignupRequest;
import com.cstudy.moduleapi.enums.MemberTestEnum;
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

public class MemberMockApiCaller extends MockApiCaller {

    public MemberMockApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    public ApiResponse<MemberLoginResponse> login() throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/member/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(MemberLoginRequest.builder()
                        .email(MemberTestEnum.VALID_EMAIL.getMessage())
                        .password(MemberTestEnum.VALID_PASSWORD.getMessage())
                        .build()));

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        MemberLoginResponse memberLoginResponse = MemberLoginResponse.builder()
                .accessToken(JsonPath.read(response.getContentAsString(), "$.accessToken"))
                .refreshToken(JsonPath.read(response.getContentAsString(), "$.refreshToken"))
                .name(JsonPath.read(response.getContentAsString(), "$.name"))
                .email(JsonPath.read(response.getContentAsString(), "$.email"))
                .build();

        return new ApiResponse<>(response.getStatus(), memberLoginResponse);
    }


    public ApiResponse<String> signup(MemberSignupRequest request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/member/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        return new ApiResponse<>(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }

    public ApiResponse<String> logout(String url, Object request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        return new ApiResponse<>(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }

    public ApiResponse<MemberLoginResponse> login(String url, Object request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        MemberLoginResponse memberLoginResponse = MemberLoginResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .name(MemberTestEnum.CUSTOM_NAME.getMessage())
                .email(MemberTestEnum.VALID_EMAIL.getMessage())
                .build();

        return ApiResponse.success(response.getStatus(), memberLoginResponse);
    }

    public ApiResponse<ErrorResponse> fail(String url, Object request) throws Exception {

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


    public ApiResponse<ErrorResponse> deleteFail(String url, Object request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + ADMIN_USER);

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

    public ApiResponse<ErrorResponse> emptyTokenlogout(String url, Object request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(JsonPath.read(mockMvc.perform(builder)
                        .andReturn()
                        .getResponse().getContentAsString(StandardCharsets.UTF_8), "$.code"))
                .message(JsonPath.read(mockMvc.perform(builder)
                        .andReturn()
                        .getResponse().getContentAsString(StandardCharsets.UTF_8), "$.message"))
                .build();

        return new ApiResponse<>(mockMvc.perform(builder)
                .andReturn()
                .getResponse().getStatus(), errorResponse);
    }


    public ApiResponse<ErrorResponse> failWithEmptyParameter(String url) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + CUSTOM_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.code"))
                .message(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.message"))
                .validation(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.detailMessage"))
                .build();

        return new ApiResponse<>(response.getStatus(), errorResponse);
    }
}
