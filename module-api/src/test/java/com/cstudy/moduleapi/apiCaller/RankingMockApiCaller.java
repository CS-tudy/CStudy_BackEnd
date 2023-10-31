package com.cstudy.moduleapi.apiCaller;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.MockApiCaller;
import com.cstudy.modulecommon.dto.NoticeResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.cstudy.moduleapi.config.ControllerTest.ADMIN_USER;

public class RankingMockApiCaller extends MockApiCaller {

    public RankingMockApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }


    public ApiResponse<String> findMemberAllAboutRankingBoard() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/ranks")
                .header("Authorization", "Bearer " + ADMIN_USER)
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        return ApiResponse.success(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }



}
