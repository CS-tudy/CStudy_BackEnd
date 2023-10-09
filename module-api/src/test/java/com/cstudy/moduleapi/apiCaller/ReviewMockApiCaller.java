package com.cstudy.moduleapi.apiCaller;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.MockApiCaller;
import com.cstudy.moduleapi.dto.notice.NoticeSaveRequestDto;
import com.cstudy.moduleapi.dto.review.ReviewNoteResponseDto;
import com.cstudy.moduleapi.dto.review.ReviewUserResponseDto;
import com.cstudy.modulecommon.dto.NoticeUpdateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.cstudy.moduleapi.config.ControllerTest.ADMIN_USER;

public class ReviewMockApiCaller extends MockApiCaller {

    public ReviewMockApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }


    public ApiResponse<ReviewUserResponseDto> findMongoAboutReviewNote() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/review")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();
        objectMapper.registerModule(new JavaTimeModule());


        ReviewUserResponseDto responseDto = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ReviewUserResponseDto.class);



        return ApiResponse.success(response.getStatus(), responseDto);
    }
}
