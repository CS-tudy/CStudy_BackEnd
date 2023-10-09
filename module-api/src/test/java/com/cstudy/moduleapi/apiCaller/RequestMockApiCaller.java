package com.cstudy.moduleapi.apiCaller;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.MockApiCaller;
import com.cstudy.moduleapi.dto.request.CreateRequestRequestDto;
import com.cstudy.moduleapi.dto.request.FlagRequestDto;
import com.cstudy.moduleapi.dto.request.RequestResponseDto;
import com.cstudy.moduleapi.dto.review.ReviewUserResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static com.cstudy.moduleapi.config.ControllerTest.ADMIN_USER;
import static com.cstudy.moduleapi.config.ControllerTest.CUSTOM_USER;

public class RequestMockApiCaller extends MockApiCaller {

    public RequestMockApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }


    public ApiResponse<String> createRequest(CreateRequestRequestDto requestDto) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .header("Authorization", "Bearer " + CUSTOM_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        return ApiResponse.success(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }

    public ApiResponse<String> updateFlag(FlagRequestDto requestDto) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.patch("/api/request/approve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        return ApiResponse.success(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }

    public ApiResponse<RequestResponseDto> getRequest(Long requestId) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/request/{requestId}",requestId)
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();


        RequestResponseDto responseDto = RequestResponseDto.builder()
                .id(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.id"))
                .title(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.title"))
                .description(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.description"))
                .memberId(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.memberId"))
                .memberName(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.memberName"))
                .flag(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.flag"))
                .createAt(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.createAt"))
                .build();

        return ApiResponse.success(response.getStatus(),responseDto);
    }

}
