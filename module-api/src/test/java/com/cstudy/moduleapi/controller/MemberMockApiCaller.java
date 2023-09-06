package com.cstudy.moduleapi.controller;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.dto.member.MemberLoginRequest;
import com.cstudy.moduleapi.dto.member.MemberLoginResponse;
import com.cstudy.moduleapi.enums.MemberTestEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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

        MemberLoginResponse memberLoginResponse =MemberLoginResponse.builder()
                .accessToken(JsonPath.read(response.getContentAsString(), "$.accessToken"))
                .refreshToken(JsonPath.read(response.getContentAsString(), "$.refreshToken"))
                .name(JsonPath.read(response.getContentAsString(), "$.name"))
                .email(JsonPath.read(response.getContentAsString(), "$.email"))
                .build();

        return new ApiResponse<>(response.getStatus(), memberLoginResponse);
    }

}
