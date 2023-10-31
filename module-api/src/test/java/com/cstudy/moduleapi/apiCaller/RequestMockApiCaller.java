package com.cstudy.moduleapi.apiCaller;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.MockApiCaller;
import com.cstudy.moduleapi.dto.request.CreateRequestRequestDto;
import com.cstudy.moduleapi.dto.request.FlagRequestDto;
import com.cstudy.moduleapi.dto.request.RequestResponseDto;
import com.cstudy.modulecommon.dto.UpdateRequestRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
                .id(((Number) JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.id")).longValue())
                .title(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.title"))
                .description(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.description"))
                .memberId(((Number) JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.memberId")).longValue())
                .memberName(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.memberName"))
                .flag(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.flag"))
                .createAt(LocalDateTime.parse(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.createAt"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();

        return ApiResponse.success(response.getStatus(),responseDto);
    }

    public ApiResponse<Page<RequestResponseDto>> getRequestList() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/request/member")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + CUSTOM_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        String jsonResponse = response.getContentAsString(StandardCharsets.UTF_8);
        objectMapper.registerModule(new JavaTimeModule());

        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        List<RequestResponseDto> content = objectMapper.readValue(jsonNode.get("content").toString(), new TypeReference<>() {});
        int totalPages = jsonNode.get("totalPages").asInt();
        long totalElements = jsonNode.get("totalElements").asLong();

        Page<RequestResponseDto> page = new PageImpl<>(content, PageRequest.of(0, content.size()), totalElements);

        return ApiResponse.success(response.getStatus(), page);
    }

    public ApiResponse<String> updateRequest(UpdateRequestRequestDto requestDto) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.patch("/api/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .header("Authorization", "Bearer " + CUSTOM_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        return ApiResponse.success(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }


    public ApiResponse<String> deleteRequestById(Long requestId) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/api/request/{requestId}",requestId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + CUSTOM_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        return ApiResponse.success(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }

}
